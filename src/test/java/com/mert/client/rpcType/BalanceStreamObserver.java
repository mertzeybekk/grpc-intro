package com.mert.client.rpcType;

import com.mert.models.Balance;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class BalanceStreamObserver implements StreamObserver<com.mert.models.Balance> {
    //BalanceStreamObserver Sınıfı:
    //
    //Bu sınıf, istemci tarafında sunucudan gelen yanıtları dinlemek için kullanılır.
    //onNext metodu: Sunucu tarafından gönderilen her Balance yanıtı için çağrılır ve bu yanıtları işler.
    //onError metodu: Hata durumunda çağrılır ve işlemi sonlandırır.
    //onCompleted metodu: Sunucu tarafındaki işlemi tamamlar.
    private CountDownLatch latch;

    public BalanceStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Balance balance) {
        System.out.println("Balance " + balance.getAmount());
    }

    @Override
    public void onError(Throwable throwable) {
        this.latch.countDown();

    }

    @Override
    public void onCompleted() {
        System.out.println("completed");
        this.latch.countDown();
    }
}
