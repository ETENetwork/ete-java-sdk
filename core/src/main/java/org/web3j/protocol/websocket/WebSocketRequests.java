/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.protocol.websocket;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.web3j.protocol.core.BatchResponse;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

/** */
public class WebSocketRequests extends WebSocketRequest<BatchResponse> {

    private List<Request<?, ? extends Response<?>>> requests;
    private long originId;

    public WebSocketRequests(
            CompletableFuture<BatchResponse> onReply,
            List<Request<?, ? extends Response<?>>> requests,
            Long originId) {

        super(onReply, BatchResponse.class);
        this.requests = requests;
        this.originId = originId;
    }

    public List<Request<?, ? extends Response<?>>> getRequests() {
        return requests;
    }

    public long getOriginId() {
        return originId;
    }
}
