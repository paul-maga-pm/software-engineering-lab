package com.salesagents.networking.protocols;

import java.io.Serializable;

public class AgentRpcRequest implements RpcRequest, Serializable {
    private AgentRpcRequestType type;
    private Object data;

    private AgentRpcRequest() {

    }

    public AgentRpcRequestType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static class RequestBuilder {
        private AgentRpcRequest instance;

        public RequestBuilder() {
            instance = new AgentRpcRequest();
        }

        public RequestBuilder setType(AgentRpcRequestType type) {
            instance.type = type;
            return this;
        }

        public RequestBuilder setData(Object data) {
            instance.data = data;
            return this;
        }

        public AgentRpcRequest build() {
            return instance;
        }
    }
}
