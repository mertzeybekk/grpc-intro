package com.mert.server.deadline;

import com.mert.server.rpcTypes.TransferService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
       Server server= ServerBuilder.forPort(6565)
                .addService(new DeadLineService())
                .build();
       server.start();
       server.awaitTermination();
    }
}
