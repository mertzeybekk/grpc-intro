package com.mert.server.rpcTypes;

import com.mert.models.TransferRequest;
import com.mert.models.TransferResponse;
import com.mert.models.TransferStatus;
import io.grpc.stub.StreamObserver;

public class TransferStreamingRequest implements StreamObserver<com.mert.models.TransferRequest> {
    private StreamObserver<com.mert.models.TransferResponse>transferResponseStreamObserver;
    private TransferResponse.Builder addAccount;

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        int fromAccount = transferRequest.getFromAccount();
        int toAccount = transferRequest.getToAccount();
        int amount = transferRequest.getAmount();
        int balance = AccountDataBase.getBalance(fromAccount);
        TransferStatus status = TransferStatus.FAILED;
        if(balance >= amount && fromAccount != toAccount){
            AccountDataBase.deductBalance(fromAccount, amount);
            AccountDataBase.addBalance(toAccount, amount);
            status = TransferStatus.SUCCESS;
        }
        com.mert.models.Account fromAccountInfo = com.mert.models.Account.newBuilder().setAccountNumber(fromAccount).setAmount(AccountDataBase.getBalance(fromAccount)).build();
        com.mert.models.Account toAccountInfo = com.mert.models.Account.newBuilder().setAccountNumber(toAccount).setAmount(AccountDataBase.getBalance(toAccount)).build();
        TransferResponse transferResponse = TransferResponse.newBuilder()
                .setStatus(status)
                .addAccounts(fromAccountInfo)
                .addAccounts(toAccountInfo)
                .build();
        this.transferResponseStreamObserver.onNext(transferResponse);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        AccountDataBase.printAccountDetails();
      this.transferResponseStreamObserver.onCompleted();
    }
}
