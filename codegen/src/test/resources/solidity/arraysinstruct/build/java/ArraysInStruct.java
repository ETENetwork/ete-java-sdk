package org.web3j.unittests.java;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes2;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.StaticArray5;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("rawtypes")
public class ArraysInStruct extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_CALLFUNCTION = "callFunction";

    @Deprecated
    protected ArraysInStruct(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ArraysInStruct(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ArraysInStruct(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ArraysInStruct(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> callFunction(List<byte[]> bytesArrayField, Entity newEntity) {
        final Function function = new Function(
                FUNC_CALLFUNCTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.DynamicBytes>(
                        org.web3j.abi.datatypes.DynamicBytes.class,
                        org.web3j.abi.Utils.typeMap(bytesArrayField, org.web3j.abi.datatypes.DynamicBytes.class)), 
                newEntity), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ArraysInStruct load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArraysInStruct(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ArraysInStruct load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArraysInStruct(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ArraysInStruct load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ArraysInStruct(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ArraysInStruct load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ArraysInStruct(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class Entity extends DynamicStruct {
        public byte[] bytesField;

        public byte[] extraData;

        public List<String> stringArrayField;

        public List<byte[]> bytesArrayField;

        public List<byte[]> bytes2ArrayField;

        public List<byte[]> bytes32ArrayField;

        public List<BigInteger> unitArrayField;

        public List<BigInteger> unit256ArrayField;

        public List<Boolean> boolField;

        public List<BigInteger> intArrayField;

        public List<String> addressArrayField;

        public List<String> stringArrayFieldStatic;

        public List<byte[]> bytesArrayFieldStatic;

        public List<byte[]> bytes2ArrayFieldStatic;

        public List<byte[]> bytes32ArrayFieldStatic;

        public List<BigInteger> unitArrayFieldStatic;

        public List<BigInteger> unit256ArrayFieldStatic;

        public List<Boolean> boolFieldStatic;

        public List<BigInteger> intArrayFieldStatic;

        public List<String> addressArrayFieldStatic;

        public Entity(byte[] bytesField, byte[] extraData, List<String> stringArrayField, List<byte[]> bytesArrayField, List<byte[]> bytes2ArrayField, List<byte[]> bytes32ArrayField, List<BigInteger> unitArrayField, List<BigInteger> unit256ArrayField, List<Boolean> boolField, List<BigInteger> intArrayField, List<String> addressArrayField, List<String> stringArrayFieldStatic, List<byte[]> bytesArrayFieldStatic, List<byte[]> bytes2ArrayFieldStatic, List<byte[]> bytes32ArrayFieldStatic, List<BigInteger> unitArrayFieldStatic, List<BigInteger> unit256ArrayFieldStatic, List<Boolean> boolFieldStatic, List<BigInteger> intArrayFieldStatic, List<String> addressArrayFieldStatic) {
            super(new org.web3j.abi.datatypes.DynamicBytes(bytesField), 
                    new org.web3j.abi.datatypes.generated.Bytes32(extraData), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                            org.web3j.abi.datatypes.Utf8String.class,
                            org.web3j.abi.Utils.typeMap(stringArrayField, org.web3j.abi.datatypes.Utf8String.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.DynamicBytes>(
                            org.web3j.abi.datatypes.DynamicBytes.class,
                            org.web3j.abi.Utils.typeMap(bytesArrayField, org.web3j.abi.datatypes.DynamicBytes.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes2>(
                            org.web3j.abi.datatypes.generated.Bytes2.class,
                            org.web3j.abi.Utils.typeMap(bytes2ArrayField, org.web3j.abi.datatypes.generated.Bytes2.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                            org.web3j.abi.datatypes.generated.Bytes32.class,
                            org.web3j.abi.Utils.typeMap(bytes32ArrayField, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(unitArrayField, org.web3j.abi.datatypes.generated.Uint256.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(unit256ArrayField, org.web3j.abi.datatypes.generated.Uint256.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Bool>(
                            org.web3j.abi.datatypes.Bool.class,
                            org.web3j.abi.Utils.typeMap(boolField, org.web3j.abi.datatypes.Bool.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Int256>(
                            org.web3j.abi.datatypes.generated.Int256.class,
                            org.web3j.abi.Utils.typeMap(intArrayField, org.web3j.abi.datatypes.generated.Int256.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                            org.web3j.abi.datatypes.Address.class,
                            org.web3j.abi.Utils.typeMap(addressArrayField, org.web3j.abi.datatypes.Address.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.Utf8String>(
                            org.web3j.abi.datatypes.Utf8String.class,
                            org.web3j.abi.Utils.typeMap(stringArrayFieldStatic, org.web3j.abi.datatypes.Utf8String.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.DynamicBytes>(
                            org.web3j.abi.datatypes.DynamicBytes.class,
                            org.web3j.abi.Utils.typeMap(bytesArrayFieldStatic, org.web3j.abi.datatypes.DynamicBytes.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Bytes2>(
                            org.web3j.abi.datatypes.generated.Bytes2.class,
                            org.web3j.abi.Utils.typeMap(bytes2ArrayFieldStatic, org.web3j.abi.datatypes.generated.Bytes2.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Bytes32>(
                            org.web3j.abi.datatypes.generated.Bytes32.class,
                            org.web3j.abi.Utils.typeMap(bytes32ArrayFieldStatic, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(unitArrayFieldStatic, org.web3j.abi.datatypes.generated.Uint256.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(unit256ArrayFieldStatic, org.web3j.abi.datatypes.generated.Uint256.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.Bool>(
                            org.web3j.abi.datatypes.Bool.class,
                            org.web3j.abi.Utils.typeMap(boolFieldStatic, org.web3j.abi.datatypes.Bool.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Int256>(
                            org.web3j.abi.datatypes.generated.Int256.class,
                            org.web3j.abi.Utils.typeMap(intArrayFieldStatic, org.web3j.abi.datatypes.generated.Int256.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.Address>(
                            org.web3j.abi.datatypes.Address.class,
                            org.web3j.abi.Utils.typeMap(addressArrayFieldStatic, org.web3j.abi.datatypes.Address.class)));
            this.bytesField = bytesField;
            this.extraData = extraData;
            this.stringArrayField = stringArrayField;
            this.bytesArrayField = bytesArrayField;
            this.bytes2ArrayField = bytes2ArrayField;
            this.bytes32ArrayField = bytes32ArrayField;
            this.unitArrayField = unitArrayField;
            this.unit256ArrayField = unit256ArrayField;
            this.boolField = boolField;
            this.intArrayField = intArrayField;
            this.addressArrayField = addressArrayField;
            this.stringArrayFieldStatic = stringArrayFieldStatic;
            this.bytesArrayFieldStatic = bytesArrayFieldStatic;
            this.bytes2ArrayFieldStatic = bytes2ArrayFieldStatic;
            this.bytes32ArrayFieldStatic = bytes32ArrayFieldStatic;
            this.unitArrayFieldStatic = unitArrayFieldStatic;
            this.unit256ArrayFieldStatic = unit256ArrayFieldStatic;
            this.boolFieldStatic = boolFieldStatic;
            this.intArrayFieldStatic = intArrayFieldStatic;
            this.addressArrayFieldStatic = addressArrayFieldStatic;
        }

        public Entity(DynamicBytes bytesField, Bytes32 extraData, DynamicArray<Utf8String> stringArrayField, DynamicArray<DynamicBytes> bytesArrayField, DynamicArray<Bytes2> bytes2ArrayField, DynamicArray<Bytes32> bytes32ArrayField, DynamicArray<Uint256> unitArrayField, DynamicArray<Uint256> unit256ArrayField, DynamicArray<Bool> boolField, DynamicArray<Int256> intArrayField, DynamicArray<Address> addressArrayField, StaticArray5<Utf8String> stringArrayFieldStatic, StaticArray5<DynamicBytes> bytesArrayFieldStatic, StaticArray5<Bytes2> bytes2ArrayFieldStatic, StaticArray5<Bytes32> bytes32ArrayFieldStatic, StaticArray5<Uint256> unitArrayFieldStatic, StaticArray5<Uint256> unit256ArrayFieldStatic, StaticArray5<Bool> boolFieldStatic, StaticArray5<Int256> intArrayFieldStatic, StaticArray5<Address> addressArrayFieldStatic) {
            super(bytesField, extraData, stringArrayField, bytesArrayField, bytes2ArrayField, bytes32ArrayField, unitArrayField, unit256ArrayField, boolField, intArrayField, addressArrayField, stringArrayFieldStatic, bytesArrayFieldStatic, bytes2ArrayFieldStatic, bytes32ArrayFieldStatic, unitArrayFieldStatic, unit256ArrayFieldStatic, boolFieldStatic, intArrayFieldStatic, addressArrayFieldStatic);
            this.bytesField = bytesField.getValue();
            this.extraData = extraData.getValue();
            this.stringArrayField = stringArrayField.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.bytesArrayField = bytesArrayField.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.bytes2ArrayField = bytes2ArrayField.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.bytes32ArrayField = bytes32ArrayField.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.unitArrayField = unitArrayField.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.unit256ArrayField = unit256ArrayField.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.boolField = boolField.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.intArrayField = intArrayField.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.addressArrayField = addressArrayField.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.stringArrayFieldStatic = stringArrayFieldStatic.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.bytesArrayFieldStatic = bytesArrayFieldStatic.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.bytes2ArrayFieldStatic = bytes2ArrayFieldStatic.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.bytes32ArrayFieldStatic = bytes32ArrayFieldStatic.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.unitArrayFieldStatic = unitArrayFieldStatic.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.unit256ArrayFieldStatic = unit256ArrayFieldStatic.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.boolFieldStatic = boolFieldStatic.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.intArrayFieldStatic = intArrayFieldStatic.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.addressArrayFieldStatic = addressArrayFieldStatic.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
        }
    }
}
