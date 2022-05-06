package com.salesagents.networking.protocols;

import java.io.Serializable;

public class AdminRpcRequest implements RpcRequest, Serializable {
    private AdminRpcRequestType type;
    private Object data;

    private AdminRpcRequest() {

    }

    public static class AdminRequestBuilder {
        private AdminRpcRequest instance;

        public AdminRequestBuilder() {
            instance = new AdminRpcRequest();
        }

        public AdminRequestBuilder setType(AdminRpcRequestType type) {
            instance.type = type;
            return this;
        }

        public AdminRequestBuilder setData(Object data) {
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
