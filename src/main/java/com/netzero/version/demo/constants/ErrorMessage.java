package com.netzero.version.demo.constants;

public class ErrorMessage {
    public enum MESSAGE {
        INF_US_0000("Success"),
        ERR_US_3333("Data not found"),
        ERR_US_9999("Error"),
        ;
        private final String msg;

        MESSAGE(String message) {
            this.msg = message;
        }

        public String getMsg() {
            return msg;
        }
    }
}
