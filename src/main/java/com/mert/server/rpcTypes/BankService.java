package com.mert.server.rpcTypes;

import com.mert.models.Balance;
import com.mert.models.BalanceCheckRequest;
import com.mert.models.BankServiceGrpc;
import com.mert.models.WithdrawRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {


    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(AccountDataBase.getBalance(accountNumber))
                .build();
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
            responseObserver.onNext(money);
            AccountDataBase.deductBalance(accountNumber, 10);
        }
        responseObserver.onCompleted();
 

    }

    @Override
    public StreamObserver<com.mert.models.DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }

}
