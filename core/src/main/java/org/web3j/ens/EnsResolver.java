/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.ens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.abi.DefaultFunctionReturnDecoder;
import org.web3j.abi.datatypes.ens.OffchainLookup;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.dto.EnsGatewayRequestDTO;
import org.web3j.dto.EnsGatewayResponseDTO;
import org.web3j.ens.contracts.generated.ENS;
import org.web3j.ens.contracts.generated.OffchainResolverContract;
import org.web3j.ens.contracts.generated.PublicResolver;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.EnsUtils;
import org.web3j.utils.Numeric;
import org.web3j.utils.Strings;

import static org.web3j.service.HSMHTTPRequestProcessor.JSON;

/** Resolution logic for contract addresses. According to https://eips.ethereum.org/EIPS/eip-2544 */
public class EnsResolver {

    private static final Logger log = LoggerFactory.getLogger(EnsResolver.class);

    public static final long DEFAULT_SYNC_THRESHOLD = 1000 * 60 * 3;

    // Permit number offchain calls  for a single contract call.
    public static final int LOOKUP_LIMIT = 4;

    public static final String REVERSE_NAME_SUFFIX = ".addr.reverse";

    private final Web3j web3j;
    private final int addressLength;
    private final TransactionManager transactionManager;

    private OkHttpClient client = new OkHttpClient();
    private long syncThreshold; // non-final in case this value needs to be tweaked

    public EnsResolver(Web3j web3j, long syncThreshold, int addressLength) {
        this.web3j = web3j;
        transactionManager = new ClientTransactionManager(web3j, null); // don't use empty string
        this.syncThreshold = syncThreshold;
        this.addressLength = addressLength;
    }

    public EnsResolver(Web3j web3j, long syncThreshold) {
        this(web3j, syncThreshold, Keys.ADDRESS_LENGTH_IN_HEX);
    }

    public EnsResolver(Web3j web3j) {
        this(web3j, DEFAULT_SYNC_THRESHOLD);
    }

    public void setSyncThreshold(long syncThreshold) {
        this.syncThreshold = syncThreshold;
    }

    public long getSyncThreshold() {
        return syncThreshold;
    }

    /**
     * Provides an access to a valid public resolver in order to access other API methods.
     *
     * @deprecated
     *     <p>Use {@link EnsResolver#obtainOffchainResolver(String)} instead.
     * @param ensName our user input ENS name
     * @return PublicResolver
     */
    @Deprecated
    protected PublicResolver obtainPublicResolver(String ensName) {
        if (isValidEnsName(ensName, addressLength)) {
            try {
                if (!isSynced()) {
                    throw new EnsResolutionException("Node is not currently synced");
                } else {
                    return lookupResolver(ensName);
                }
            } catch (Exception e) {
                throw new EnsResolutionException("Unable to determine sync status of node", e);
            }
        } else {
            throw new EnsResolutionException("EnsName is invalid: " + ensName);
        }
    }

    /**
     * Provides an access to a valid offchain resolver in order to access other API methods.
     *
     * @param ensName our user input ENS name
     * @return OffchainResolver
     */
    protected OffchainResolverContract obtainOffchainResolver(String ensName) {
        if (isValidEnsName(ensName, addressLength)) {
            try {
                if (!isSynced()) {
                    throw new EnsResolutionException("Node is not currently synced");
                } else {
                    return lookupOffchainResolver(ensName);
                }
            } catch (Exception e) {
                throw new EnsResolutionException("Unable to determine sync status of node", e);
            }
        } else {
            throw new EnsResolutionException("EnsName is invalid: " + ensName);
        }
    }

    /**
     * Returns the address of the resolver for the specified node.
     *
     * @param ensName The specified node.
     * @return address of the resolver.
     */
    public String resolve(String ensName) {
        if (Strings.isBlank(ensName) || (ensName.trim().length() == 1 && ensName.contains("."))) {
            return null;
        }

        try {
            if (isValidEnsName(ensName, addressLength)) {
                OffchainResolverContract resolver = obtainOffchainResolver(ensName);

                boolean supportWildcard =
                        resolver.supportsInterface(EnsUtils.ENSIP_10_INTERFACE_ID).send();
                byte[] nameHash = NameHash.nameHashAsBytes(ensName);

                String resolvedName;
                if (supportWildcard) {
                    String dnsEncoded = NameHash.dnsEncode(ensName);
                    String addrFunction = resolver.addr(nameHash).encodeFunctionCall();

                    String lookupDataHex =
                            resolver.resolve(
                                            Numeric.hexStringToByteArray(dnsEncoded),
                                            Numeric.hexStringToByteArray(addrFunction))
                                    .send();

                    resolvedName = resolveOffchain(lookupDataHex, resolver, LOOKUP_LIMIT);
                } else {
                    try {
                        resolvedName = resolver.addr(nameHash).send();
                    } catch (Exception e) {
                        throw new RuntimeException("Unable to execute Ethereum request: ", e);
                    }
                }

                if (!WalletUtils.isValidAddress(resolvedName)) {
                    throw new EnsResolutionException(
                            "Unable to resolve address for name: " + ensName);
                } else {
                    return resolvedName;
                }

            } else {
                return ensName;
            }
        } catch (Exception e) {
            throw new EnsResolutionException(e);
        }
    }

    protected String resolveOffchain(
            String lookupData, OffchainResolverContract resolver, int lookupCounter)
            throws Exception {
        if (EnsUtils.isEIP3668(lookupData)) {

            OffchainLookup offchainLookup =
                    OffchainLookup.build(Numeric.hexStringToByteArray(lookupData.substring(10)));

            if (!resolver.getContractAddress().equals(offchainLookup.getSender())) {
                throw new EnsResolutionException(
                        "Cannot handle OffchainLookup raised inside nested call");
            }

            String gatewayResult =
                    ccipReadFetch(
                            offchainLookup.getUrls(),
                            offchainLookup.getSender(),
                            Numeric.toHexString(offchainLookup.getCallData()));

            if (gatewayResult == null) {
                throw new EnsResolutionException("CCIP Read disabled or provided no URLs.");
            }

            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            EnsGatewayResponseDTO gatewayResponseDTO =
                    objectMapper.readValue(gatewayResult, EnsGatewayResponseDTO.class);

            String resolvedNameHex =
                    resolver.resolveWithProof(
                                    Numeric.hexStringToByteArray(gatewayResponseDTO.getData()),
                                    offchainLookup.getExtraData())
                            .send();

            // This protocol can result in multiple lookups being requested by the same contract.
            if (EnsUtils.isEIP3668(resolvedNameHex)) {
                if (lookupCounter <= 0) {
                    throw new EnsResolutionException("Lookup calls is out of limit.");
                }

                return resolveOffchain(lookupData, resolver, --lookupCounter);
            } else {
                byte[] resolvedNameBytes =
                        DefaultFunctionReturnDecoder.decodeDynamicBytes(resolvedNameHex);

                return DefaultFunctionReturnDecoder.decodeAddress(
                        Numeric.toHexString(resolvedNameBytes));
            }
        }

        return lookupData;
    }

    protected String ccipReadFetch(List<String> urls, String sender, String data) {
        List<String> errorMessages = new ArrayList<>();

        for (String url : urls) {
            Request request;
            try {
                request = buildRequest(url, sender, data);
            } catch (JsonProcessingException | EnsResolutionException e) {
                log.error(e.getMessage(), e);
                break;
            }

            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody == null) {
                        log.warn("Response body is null, url: {}", url);
                        break;
                    }

                    return new BufferedReader(new InputStreamReader(responseBody.byteStream()))
                            .lines()
                            .collect(Collectors.joining("\n"));
                } else {
                    int statusCode = response.code();
                    // 4xx indicates the result is not present; stop
                    if (statusCode >= 400 && statusCode < 500) {
                        log.error(
                                "Response error during CCIP fetch: url {}, error: {}",
                                url,
                                response.message());
                        throw new EnsResolutionException(response.message());
                    }

                    // 5xx indicates server issue; try the next url
                    errorMessages.add(response.message());

                    log.warn(
                            "Response error 500 during CCIP fetch: url {}, error: {}",
                            url,
                            response.message());
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        log.warn(Arrays.toString(errorMessages.toArray()));
        return null;
    }

    protected Request buildRequest(String url, String sender, String data)
            throws JsonProcessingException {
        if (sender == null || !WalletUtils.isValidAddress(sender)) {
            throw new EnsResolutionException("Sender address is null or not valid");
        }
        if (data == null) {
            throw new EnsResolutionException("Data is null");
        }
        if (!url.contains("{sender}")) {
            throw new EnsResolutionException("Url is not valid, sender parameter is not exist");
        }

        // URL expansion
        String href = url.replace("{sender}", sender).replace("{data}", data);

        Request.Builder builder = new Request.Builder().url(href);

        if (url.contains("{data}")) {
            return builder.get().build();
        } else {
            EnsGatewayRequestDTO requestDTO = new EnsGatewayRequestDTO(data);
            ObjectMapper om = ObjectMapperFactory.getObjectMapper();

            return builder.post(RequestBody.create(om.writeValueAsString(requestDTO), JSON))
                    .addHeader("Content-Type", "application/json")
                    .build();
        }
    }

    /**
     * Reverse name resolution as documented in the <a
     * href="https://docs.ens.domains/contract-api-reference/reverseregistrar">specification</a>.
     *
     * @param address an ethereum address, example: "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e"
     * @return a EnsName registered for provided address
     */
    public String reverseResolve(String address) {
        if (WalletUtils.isValidAddress(address, addressLength)) {
            String reverseName = Numeric.cleanHexPrefix(address) + REVERSE_NAME_SUFFIX;
            PublicResolver resolver = obtainOffchainResolver(reverseName);

            byte[] nameHash = NameHash.nameHashAsBytes(reverseName);
            String name;
            try {
                name = resolver.name(nameHash).send();
            } catch (Exception e) {
                throw new RuntimeException("Unable to execute Ethereum request", e);
            }

            if (!isValidEnsName(name, addressLength)) {
                throw new RuntimeException("Unable to resolve name for address: " + address);
            } else {
                return name;
            }
        } else {
            throw new EnsResolutionException("Address is invalid: " + address);
        }
    }

    private PublicResolver lookupResolver(String ensName) throws Exception {
        return PublicResolver.load(
                getResolverAddress(ensName), web3j, transactionManager, new DefaultGasProvider());
    }

    private OffchainResolverContract lookupOffchainResolver(String ensName) throws Exception {
        return OffchainResolverContract.load(
                getResolverAddress(ensName), web3j, transactionManager, new DefaultGasProvider());
    }

    private String getResolverAddress(String ensName) throws Exception {
        NetVersion netVersion = web3j.netVersion().send();
        String registryContract = Contracts.resolveRegistryContract(netVersion.getNetVersion());

        ENS ensRegistry =
                ENS.load(registryContract, web3j, transactionManager, new DefaultGasProvider());

        byte[] nameHash = NameHash.nameHashAsBytes(ensName);
        String address = ensRegistry.resolver(nameHash).send();

        if (EnsUtils.isAddressEmpty(address)) {
            address = getResolverAddress(EnsUtils.getParent(ensName));
        }

        return address;
    }

    boolean isSynced() throws Exception {
        EthSyncing ethSyncing = web3j.ethSyncing().send();
        if (ethSyncing.isSyncing()) {
            return false;
        } else {
            EthBlock ethBlock =
                    web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            long timestamp = ethBlock.getBlock().getTimestamp().longValue() * 1000;

            return System.currentTimeMillis() - syncThreshold < timestamp;
        }
    }

    public static boolean isValidEnsName(String input) {
        return isValidEnsName(input, Keys.ADDRESS_LENGTH_IN_HEX);
    }

    public static boolean isValidEnsName(String input, int addressLength) {
        return input != null // will be set to null on new Contract creation
                && (input.contains(".") || !WalletUtils.isValidAddress(input, addressLength));
    }

    public void setHttpClient(OkHttpClient client) {
        this.client = client;
    }
}
