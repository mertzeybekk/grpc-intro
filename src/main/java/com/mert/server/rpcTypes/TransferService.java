package com.mert.server.rpcTypes;

import com.mert.models.TransferRequest;
import com.mert.models.TransferResponse;
import io.grpc.stub.StreamObserver;

public class TransferService extends com.mert.models.TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}
