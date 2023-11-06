package com.mert.server.metaData;

import io.grpc.Metadata;

public class ServerConstants {
    public static final Metadata.Key<String>TOKEN = Metadata.Key.of("client-token",
            Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String>USER_TOKEN = Metadata.Key.of("user-token",
            Metadata.ASCII_STRING_MARSHALLER);
}
