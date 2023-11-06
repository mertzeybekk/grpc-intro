package com.mert.server.rpcTypes;

import com.mert.models.Balance;
import com.mert.models.DepositRequest;
import io.grpc.stub.StreamObserver;

public class CashStreamingRequest implements StreamObserver<com.mert.models.DepositRequest> {
    /*
    CashStreamingRequest Sınıfı:

Bu sınıf, sunucu tarafında gRPC istemcisini temsil eder. Yani, istemci tarafından sunucuya gönderilen veriyi işler ve sunucudan gelen yanıtları alır.
CashStreamingRequest, bir StreamObserver sınıfını uygular ve bu nesneyi kullanarak sunucu tarafından gönderilen yanıtları alır.
onNext metodu: Sunucu tarafından gönderilen her DepositRequest için çağrılır. Bu metod, DepositRequest mesajını alır, işler ve hesap bakiyesini günceller.
onError metodu: Hata durumunda çağrılır. Bu örnekte hata işlenmez.
onCompleted metodu: Sunucu tarafındaki işlemi tamamlar. Güncel hesap bakiyesini Balance mesajı ile yanıt olarak gönderir ve işlemi sonlandırır.
     */
    private StreamObserver<com.mert.models.Balance>balanceStreamObserver;
    private int accountBalance;

    public CashStreamingRequest(StreamObserver<Balance> balanceStreamObserver) {
        this.balanceStreamObserver = balanceStreamObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        int accountNumber = depositRequest.getAccountNumber();
        int amount = depositRequest.getAmount();
        this.accountBalance = AccountDataBase.addBalance(accountNumber,amount);

    }

    @Override
    public void onError(Throwable throwable) {
         throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {
        com.mert.models.Balance balance = com.mert.models.Balance.newBuilder().setAmount(this.accountBalance).build();
        this.balanceStreamObserver.onNext(balance);
        this.balanceStreamObserver.onCompleted();
    }
}
