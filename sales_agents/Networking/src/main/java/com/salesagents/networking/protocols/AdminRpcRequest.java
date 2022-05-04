package com.salesagents.networking.protocols;

import java.io.Serializable;

public class AdminRpcRequest implements RpcRequest, Serializable {
    private AdminRpcRequestType type;
    private Object data;

    private AdminRpcRequest() {

    }

    public static class RequestBuilder {
        private AdminRpcRequest instance;

        public RequestBuilder() {
            instance = new AdminRpcRequest();
        }

        public RequestBuilder setType(AdminRpcRequestType type) {
            instance.type = type;
            return this;
        }

        public RequestBuilder setData(Object data) {
            instance.data = data;
            return this;
        }

        public AdminRpcRequest build() {
            return instance;
        }
    }

    public AdminRpcRequestType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
