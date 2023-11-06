package com.mert.server.deadline;

import com.google.common.util.concurrent.Uninterruptibles;
import com.mert.models.Balance;
import com.mert.models.BalanceCheckRequest;
import com.mert.models.BankServiceGrpc;
import com.mert.models.WithdrawRequest;
import com.mert.server.rpcTypes.AccountDataBase;
import com.mert.server.rpcTypes.CashStreamingRequest;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class DeadLineService extends BankServiceGrpc.BankServiceImplBase {


    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(AccountDataBase.getBalance(accountNumber))
                .build();
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);  // 3 saniye süre koyduk server dan cevap gelmesi için
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<com.mert.models.Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();
        int balance = AccountDataBase.getBalance(accountNumber);
        if (balance < amount) {
            Status status = Status.FAILED_PRECONDITION.withDescription("No enough money.you have only" + balance);
            responseObserver.onError(status.asRuntimeException());
        }
        for (int i = 0; i < (amount / 10); i++) {
            com.mert.models.Money money = com.mert.models.Money.newBuilder().setValue(10).build();
            Uninterruptibles.sleepUninterruptibly(3,TimeUnit.SECONDS);
            if(!Context.current().isCancelled()){
                // Burada Yapılan Kısım client tarafından withDeadline süre tanımladık diyelim 3 saniyede dönsün diye eğer tüm response
                //belirlenen sürede dönmzse server bakıyor client tarafından süre aşıldı mı diye evetse server işlemi yapmıypr
                responseObserver.onNext(money);
                AccountDataBase.deductBalance(accountNumber, 10);
            }else {
                break;
            }

        }
        responseObserver.onCompleted();
 

    }

    @Override
    public StreamObserver<com.mert.models.DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }

}
