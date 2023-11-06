package com.mert.client.rpcType;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {

    private com.mert.models.TransferServiceGrpc.TransferServiceStub stub;

    @BeforeAll
    public void setup(){
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        this.stub = com.mert.models.TransferServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void transfer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        TransferStreamingResponse response = new TransferStreamingResponse(latch);
        StreamObserver<com.mert.models.TransferRequest> requestStreamObserver = this.stub.transfer(response);

        for (int i = 0; i < 100; i++) {
            com.mert.models.TransferRequest request = com.mert.models.TransferRequest.newBuilder()
                    .setFromAccount(ThreadLocalRandom.current().nextInt(1, 11))
                    .setToAccount(ThreadLocalRandom.current().nextInt(1, 11))
                    .setAmount(ThreadLocalRandom.current().nextInt(1, 21))
                    .build();
            requestStreamObserver.onNext(request);
        }
        requestStreamObserver.onCompleted();
        latch.await();
    }


}
