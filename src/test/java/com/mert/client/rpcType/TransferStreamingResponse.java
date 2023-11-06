package com.mert.client.rpcType;

import com.mert.models.TransferResponse;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class TransferStreamingResponse implements StreamObserver<com.mert.models.TransferResponse> {
    private CountDownLatch latch;

    public TransferStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(TransferResponse transferResponse) {
        System.out.println("Status :" + transferResponse.getStatus());
        transferResponse.getAccountsList()
                .stream()
                .map(account -> account.getAccountNumber() + " : " + account.getAmount())
                .forEach(System.out::println);

    }

    @Override
    public void onError(Throwable throwable) {
           this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println(
                "Transfer of success"
        );
        this.latch.countDown();;
    }
}
