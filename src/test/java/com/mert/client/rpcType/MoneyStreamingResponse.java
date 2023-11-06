package com.mert.client.rpcType;

import com.mert.models.Money;
import io.grpc.stub.StreamObserver;

public class MoneyStreamingResponse implements StreamObserver<com.mert.models.Money> {
    @Override
    public void onNext(Money money) {
        System.out.println("Received async : " + money.getValue());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(
                throwable.getMessage()
        );

    }

    @Override
    public void onCompleted() {
        System.out.println(
                "Done!!"
        );
    }
    //para akışı sağlandıktan sonra bu yapı çalışır
}
