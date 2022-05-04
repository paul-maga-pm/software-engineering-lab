package com.salesagents.networking.protocols;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private RpcResponseType type;
    private Object data;

    private RpcResponse() {

    }

    public RpcResponseType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public boolean isUpdateNotification() {
        return type == RpcResponseType.PRODUCT_WAS_ADDED ||
                type == RpcResponseType.PRODUCT_WAS_REMOVED ||
                type == RpcResponseType.PRODUCT_WAS_UPDATED;
    }

    public static class ResponseBuilder {
        private RpcResponse instance;

        public ResponseBuilder() {
            instance = new RpcResponse();
        }

        public ResponseBuilder setType(RpcResponseType type) {
            instance.type = type;
            return this;
        }

        public ResponseBuilder setData(Object data) {
            instance.data = data;
            return this;
        }

        public RpcResponse build() {
            return instance;
        }
    }
}
