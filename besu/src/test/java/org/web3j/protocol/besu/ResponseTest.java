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
package org.web3j.protocol.besu;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.besu.response.BesuEthAccountsMapResponse;
import org.web3j.protocol.besu.response.BesuFullDebugTraceResponse;
import org.web3j.protocol.besu.response.BesuSignerMetrics;
import org.web3j.protocol.besu.response.FullDebugTraceInfo;
import org.web3j.protocol.besu.response.privacy.PrivCreatePrivacyGroup;
import org.web3j.protocol.besu.response.privacy.PrivFindPrivacyGroup;
import org.web3j.protocol.besu.response.privacy.PrivGetPrivacyPrecompileAddress;
import org.web3j.protocol.besu.response.privacy.PrivGetPrivateTransaction;
import org.web3j.protocol.besu.response.privacy.PrivGetTransactionReceipt;
import org.web3j.protocol.besu.response.privacy.PrivacyGroup;
import org.web3j.protocol.besu.response.privacy.PrivateEnclaveKey;
import org.web3j.protocol.besu.response.privacy.PrivateTransactionLegacy;
import org.web3j.protocol.besu.response.privacy.PrivateTransactionReceipt;
import org.web3j.protocol.besu.response.privacy.PrivateTransactionWithPrivacyGroup;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.utils.Base64String;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ResponseTest extends ResponseTester {

    @Test
    public void testClicqueGetSigners() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": [\"0x42eb768f2244c8811c63729a21a3569731535f06\","
                        + "\"0x7ffc57839b00206d1ad20c69a1981b489f772031\","
                        + "\"0xb279182d99e65703f0076e4812653aab85fca0f0\"]\n"
                        + "}");

        EthAccounts ethAccounts = deserialiseResponse(EthAccounts.class);
        assertEquals(
                ethAccounts.getAccounts().toString(),
                ("[0x42eb768f2244c8811c63729a21a3569731535f06, "
                        + "0x7ffc57839b00206d1ad20c69a1981b489f772031, "
                        + "0xb279182d99e65703f0076e4812653aab85fca0f0]"));
    }

    @Test
    public void testClicqueProposals() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": {\"0x42eb768f2244c8811c63729a21a3569731535f07\": false,"
                        + "\"0x12eb759f2222d7711c63729a45c3585731521d01\": true}\n}");

        BesuEthAccountsMapResponse mapResponse =
                deserialiseResponse(BesuEthAccountsMapResponse.class);
        assertEquals(
                mapResponse.getAccounts().toString(),
                ("{0x42eb768f2244c8811c63729a21a3569731535f07=false, "
                        + "0x12eb759f2222d7711c63729a45c3585731521d01=true}"));
    }

    @Test
    public void testIbftGetValidatorMetrics() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": [\n"
                        + "{\"address\": \"0x42eb768f2244c8811c63729a21a3569731535f07\",\n"
                        + "\"proposedBlockCount\": \"0x0\",\n"
                        + "\"lastProposedBlockNumber\": \"0x1\"}\n"
                        + "]\n"
                        + "}");

        BesuSignerMetrics signerMetrics = deserialiseResponse(BesuSignerMetrics.class);
        assertEquals(
                signerMetrics.getSignerMetrics().get(0).getAddress(),
                "0x42eb768f2244c8811c63729a21a3569731535f07");

        assertEquals(
                signerMetrics.getSignerMetrics().get(0).getProposedBlockCount(), BigInteger.ZERO);

        assertEquals(
                signerMetrics.getSignerMetrics().get(0).getLastProposedBlockNumber(),
                BigInteger.ONE);
    }

    @Test
    public void testPrivGetPrivateTransactionLegacy() {

        buildResponse(
                "{\n"
                        + "    \"id\":1,\n"
                        + "    \"jsonrpc\":\"2.0\",\n"
                        + "    \"result\": {\n"
                        + "        \"hash\":\"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b\",\n"
                        + "        \"nonce\":\"0x0\",\n"
                        + "        \"from\":\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"to\":\"0x85h43d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"value\":\"0x7f110\",\n"
                        + "        \"gas\": \"0x7f110\",\n"
                        + "        \"gasPrice\":\"0x9184e72a000\",\n"
                        + "        \"input\":\"0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360\",\n"
                        + "        \"r\":\"0xf115cc4d7516dd430046504e1c888198e0323e8ded016d755f89c226ba3481dc\",\n"
                        + "        \"s\":\"0x4a2ae8ee49f1100b5c0202b37ed8bacf4caeddebde6b7f77e12e7a55893e9f62\",\n"
                        + "        \"v\":\"0x0\",\n"
                        + "        \"privateFrom\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "        \"privateFor\":[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=\"],\n"
                        + "        \"restriction\":\"restricted\""
                        + "  }\n"
                        + "}");
        PrivateTransactionLegacy privateTransaction =
                new PrivateTransactionLegacy(
                        "0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b",
                        "0x0",
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                        "0x85h43d8a49eeb85d32cf465507dd71d507100c1",
                        "0x7f110",
                        "0x7f110",
                        "0x9184e72a000",
                        "0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360",
                        "0xf115cc4d7516dd430046504e1c888198e0323e8ded016d755f89c226ba3481dc",
                        "0x4a2ae8ee49f1100b5c0202b37ed8bacf4caeddebde6b7f77e12e7a55893e9f62",
                        "0x0",
                        Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="),
                        Base64String.wrapList(
                                "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                                "Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs="),
                        "restricted");

        PrivGetPrivateTransaction privPrivateTransaction =
                deserialiseResponse(PrivGetPrivateTransaction.class);
        assertEquals(privPrivateTransaction.getPrivateTransaction().get(), (privateTransaction));
    }

    @Test
    public void testPrivGetPrivateTransactionPrivacyGroup() {
        buildResponse(
                "{\n"
                        + "    \"id\":1,\n"
                        + "    \"jsonrpc\":\"2.0\",\n"
                        + "    \"result\": {\n"
                        + "        \"hash\":\"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b\",\n"
                        + "        \"nonce\":\"0x0\",\n"
                        + "        \"from\":\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"to\":\"0x85h43d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"value\":\"0x7f110\",\n"
                        + "        \"gas\": \"0x7f110\",\n"
                        + "        \"gasPrice\":\"0x9184e72a000\",\n"
                        + "        \"input\":\"0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360\",\n"
                        + "        \"r\":\"0xf115cc4d7516dd430046504e1c888198e0323e8ded016d755f89c226ba3481dc\",\n"
                        + "        \"s\":\"0x4a2ae8ee49f1100b5c0202b37ed8bacf4caeddebde6b7f77e12e7a55893e9f62\",\n"
                        + "        \"v\":\"0x0\",\n"
                        + "        \"privateFrom\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "        \"privateFor\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "        \"restriction\":\"restricted\""
                        + "  }\n"
                        + "}");
        PrivateTransactionWithPrivacyGroup privateTransaction =
                new PrivateTransactionWithPrivacyGroup(
                        "0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b",
                        "0x0",
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                        "0x85h43d8a49eeb85d32cf465507dd71d507100c1",
                        "0x7f110",
                        "0x7f110",
                        "0x9184e72a000",
                        "0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360",
                        "0xf115cc4d7516dd430046504e1c888198e0323e8ded016d755f89c226ba3481dc",
                        "0x4a2ae8ee49f1100b5c0202b37ed8bacf4caeddebde6b7f77e12e7a55893e9f62",
                        "0x0",
                        Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="),
                        Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="),
                        "restricted");

        PrivGetPrivateTransaction privPrivateTransaction =
                deserialiseResponse(PrivGetPrivateTransaction.class);
        assertEquals(privPrivateTransaction.getPrivateTransaction().get(), (privateTransaction));
    }

    @Test
    public void testPrivGetPrivateTransactionNull() {
        buildResponse("{\n" + "  \"result\": null\n" + "}");

        PrivGetPrivateTransaction privPrivateTransaction =
                deserialiseResponse(PrivGetPrivateTransaction.class);
        assertEquals(privPrivateTransaction.getPrivateTransaction(), (Optional.empty()));
    }

    @Test
    public void testPrivDistributeRawTransaction() {

        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\"\n"
                        + "}");

        PrivateEnclaveKey enclaveKey = deserialiseResponse(PrivateEnclaveKey.class);
        assertEquals(
                enclaveKey.getKey(),
                ("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331"));
    }

    @Test
    public void testPrivGetPrivacyPrecompileAddress() {

        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"0xb60e8dd61c5d32be8058bb8eb970870f07233155\"\n"
                        + "}");

        PrivGetPrivacyPrecompileAddress privGetPrivacyPrecompileAddress =
                deserialiseResponse(PrivGetPrivacyPrecompileAddress.class);
        assertEquals(
                privGetPrivacyPrecompileAddress.getAddress(),
                ("0xb60e8dd61c5d32be8058bb8eb970870f07233155"));
    }

    @Test
    public void testPrivCreatePrivacyGroup() {

        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\"\n"
                        + "}");

        PrivCreatePrivacyGroup privCreatePrivacyGroup =
                deserialiseResponse(PrivCreatePrivacyGroup.class);
        assertEquals(
                privCreatePrivacyGroup.getPrivacyGroupId().toString(),
                ("DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w="));
    }

    @Test
    public void testPrivDeletePrivacyGroup() {

        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"true\"\n"
                        + "}");

        BooleanResponse privDeletePrivacyGroup = deserialiseResponse(BooleanResponse.class);
        assertEquals(privDeletePrivacyGroup.success(), (true));
    }

    @Test
    public void testPrivFindPrivacyGroup() {

        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": [\n"
                        + "         {\n"
                        + "            \"privacyGroupId\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "            \"name\":\"PrivacyGroupName\",\n"
                        + "            \"description\":\"PrivacyGroupDescription\",\n"
                        + "            \"type\":\"LEGACY\",\n"
                        + "            \"members\": [\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\"]\n"
                        + "         },\n"
                        + "         {\n"
                        + "            \"privacyGroupId\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "            \"name\":\"PrivacyGroupName\",\n"
                        + "            \"description\":\"PrivacyGroupDescription\",\n"
                        + "            \"type\":\"PANTHEON\",\n"
                        + "            \"members\": [\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\"]\n"
                        + "         },\n"
                        + "         {\n"
                        + "            \"privacyGroupId\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "            \"type\":\"ONCHAIN\",\n"
                        + "            \"members\": [\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\"]\n"
                        + "         }\n"
                        + "    ]\n"
                        + "}");

        PrivacyGroup privacyGroup1 =
                new PrivacyGroup(
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        PrivacyGroup.Type.LEGACY,
                        "PrivacyGroupName",
                        "PrivacyGroupDescription",
                        Base64String.wrapList("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="));
        PrivacyGroup privacyGroup2 =
                new PrivacyGroup(
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        PrivacyGroup.Type.PANTHEON,
                        "PrivacyGroupName",
                        "PrivacyGroupDescription",
                        Base64String.wrapList("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="));
        PrivacyGroup privacyGroup3 =
                new PrivacyGroup(
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        PrivacyGroup.Type.ONCHAIN,
                        null,
                        null,
                        Base64String.wrapList("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="));

        PrivFindPrivacyGroup privFindPrivacyGroup = deserialiseResponse(PrivFindPrivacyGroup.class);
        assertEquals(
                privFindPrivacyGroup.getGroups(),
                (Arrays.asList(privacyGroup1, privacyGroup2, privacyGroup3)));
    }

    @Test
    public void testPrivGetTransactionReceipt() {

        buildResponse(
                "{\n"
                        + "    \"id\":1,\n"
                        + "    \"jsonrpc\":\"2.0\",\n"
                        + "    \"result\": {\n"
                        + "        \"contractAddress\": \"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\n"
                        + "        \"from\":\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"to\":\"0x85h43d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"output\":\"myRlpEncodedOutputFromPrivateContract\",\n"
                        + "        \"status\":\"0x1\",\n"
                        + "        \"privacyGroupId\":\"Qlv2Jhn3C3/2KrPU7Jeu/9F6rElio9LNBSieb0Xk/Ro=\",\n"
                        + "        \"commitmentHash\": \"0x75aaac4be865057a576872587c9672197f1bab25e64b588c81f483c5869e0fa7\",\n"
                        + "        \"transactionHash\": \"0x5504d87dc6c6ab8ea4f5c988bcf1c41d40e6b594b80849d4444c432099ee6c34\",\n"
                        + "        \"privateFrom\": \"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "        \"logs\": [{\n"
                        + "            \"removed\": false,\n"
                        + "            \"logIndex\": \"0x1\",\n"
                        + "            \"transactionIndex\": \"0x0\",\n"
                        + "            \"transactionHash\": \"0xdf829c5a142f1fccd7d8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcf\",\n"
                        + "            \"blockHash\": \"0x8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcfdf829c5a142f1fccd7d\",\n"
                        + "            \"blockNumber\":\"0x1b4\",\n"
                        + "            \"address\": \"0x16c5785ac562ff41e2dcfdf829c5a142f1fccd7d\",\n"
                        + "            \"data\":\"0x0000000000000000000000000000000000000000000000000000000000000000\",\n"
                        + "            \"type\":\"mined\",\n"
                        + "            \"topics\": [\"0x59ebeb90bc63057b6515673c3ecf9438e5058bca0f92585014eced636878c9a5\"]"
                        + "        }]\n"
                        + "    }\n"
                        + "}");

        PrivateTransactionReceipt transactionReceipt =
                new PrivateTransactionReceipt(
                        "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                        "0x85h43d8a49eeb85d32cf465507dd71d507100c1",
                        "myRlpEncodedOutputFromPrivateContract",
                        Collections.singletonList(
                                new Log(
                                        false,
                                        "0x1",
                                        "0x0",
                                        "0xdf829c5a142f1fccd7d8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcf",
                                        "0x8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcfdf829c5a142f1fccd7d",
                                        "0x1b4",
                                        "0x16c5785ac562ff41e2dcfdf829c5a142f1fccd7d",
                                        "0x0000000000000000000000000000000000000000000000000000000000000000",
                                        "mined",
                                        Arrays.asList(
                                                "0x59ebeb90bc63057b6515673c3ecf9438e5058bca0f92585014eced636878c9a5"))),
                        "0x75aaac4be865057a576872587c9672197f1bab25e64b588c81f483c5869e0fa7",
                        "0x5504d87dc6c6ab8ea4f5c988bcf1c41d40e6b594b80849d4444c432099ee6c34",
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        null,
                        "Qlv2Jhn3C3/2KrPU7Jeu/9F6rElio9LNBSieb0Xk/Ro=",
                        "0x1",
                        null);

        PrivGetTransactionReceipt privGetTransactionReceipt =
                deserialiseResponse(PrivGetTransactionReceipt.class);

        assertEquals(privGetTransactionReceipt.getTransactionReceipt().get(), (transactionReceipt));
    }

    @Test
    public void testFullDebugTraceInfo() {
        buildResponse(
                "{\n"
                        + "\"jsonrpc\": \"2.0\",\n"
                        + "\"id\": 1,\n"
                        + "\"result\": {\n"
                        + "  \"gas\":35956,"
                        + "  \"returnValue\":\"1\","
                        + "  \"structLogs\":[\n"
                        + "      {\"depth\":0,\"error\":\"\",\"gas\":1478712,\"gasCost\":3,\"memory\":[],\"op\":\"PUSH1\",\"pc\":0,\"stack\":[],\"storage\":{}},"
                        + "      {\"depth\":0,\"error\":\"\",\"gas\":1478709,\"gasCost\":3,\"memory\":[],\"op\":\"PUSH1\",\"pc\":2,\"stack\":[\"0000000000000000000000000000000000000000000000000000000000000080\"],\"storage\":{}},"
                        + "      {\"depth\":0,\"error\":\"\",\"gas\":1477248,\"gasCost\":3,\"memory\":[\"0000000000000000000000000000000000000000000000000000000000000000\",\"0000000000000000000000000000000000000000000000000000000000000000\",\"0000000000000000000000000000000000000000000000000000000000000080\"],\"op\":\"DUP3\",\"pc\":6173,\"stack\":[\"00000000000000000000000000000000000000000000000000000000a0712d68\",\"0000000000000000000000000000000000000000000000000000000000000279\",\"00000000000000000000000000000000000000000000016929fc579f2cf60000\",\"00000000000000000000000000000000000000000000000000000000000006e5\",\"000000000000000000000000bed92733f5549af6411355d5fe12781744248f96\",\"00000000000000000000000000000000000000000000016929fc579f2cf60000\",\"00000000000000000000000000000000000000000000016929fc579f2cf60000\",\"0000000000000000000000000000000000000000000000000000000000000002\",\"0000000000000000000000000000000000000000000000000000000000000000\",\"0000000000000000000000000000000000000000000000000000000000000f31\",\"00000000000000000000000000000000000000000000016929fc579f2cf60000\",\"000000000000000000000000000000000000000000544a2efc54e6eb8bd90400\",\"0000000000000000000000000000000000000000000000000000000000000000\",\"fffffffffffffffffffffffffffffffffffffffffffffe96d603a860d309ffff\"],\"storage\":{\"0000000000000000000000000000000000000000000000000000000000000002\":\"000000000000000000000000000000000000000000544a2efc54e6eb8bd90400\"}}"
                        + "  ]"
                        + "}\n"
                        + "}");

        BesuFullDebugTraceResponse response = deserialiseResponse(BesuFullDebugTraceResponse.class);
        FullDebugTraceInfo result = response.getResult();

        assertFalse(result.getFailed());
        assertEquals(result.getGas(), 35956);
        assertEquals(result.getReturnValue(), "1");
        assertEquals(result.getStructLogs().get(0).getPc(), 0);
        assertEquals(
                result.getStructLogs()
                        .get(2)
                        .getStorage()
                        .get(
                                new BigInteger(
                                        "0000000000000000000000000000000000000000000000000000000000000002")),
                "000000000000000000000000000000000000000000544a2efc54e6eb8bd90400");
    }
}
