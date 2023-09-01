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
package org.web3j.codegen.unit.gen.kotlin;

import java.util.LinkedHashMap;
import java.util.Map;

import com.squareup.kotlinpoet.FunSpec;
import org.junit.jupiter.api.Test;

import org.web3j.protocol.Web3j;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunSpecGeneratorTest {

    @Test
    public void testGenerate() {

        String javaPoetStringFormat1 = "val %L =  %S";
        Object[] replacementValues1 = new Object[] {"hello ", "Hello how are you"};
        String javaPoetStringFormat2 = "val %L = %T.build()";
        Object[] replacementValues2 = new Object[] {"web3j", Web3j.class};
        Map<String, Object[]> statementBody = new LinkedHashMap<>();
        statementBody.put(javaPoetStringFormat1, replacementValues1);
        statementBody.put(javaPoetStringFormat2, replacementValues2);
        FunSpecGenerator funSpecGenerator = new FunSpecGenerator("test", statementBody);
        FunSpec generatedFunSpec = funSpecGenerator.generate();
        assertEquals(
                generatedFunSpec.toString(),
                "@org.junit.jupiter.api.Test\n"
                        + "fun test() {\n"
                        + "  val hello  =  \"Hello how are you\"\n"
                        + "  val web3j = org.web3j.protocol.Web3j.build()\n"
                        + "}\n");
    }
}
