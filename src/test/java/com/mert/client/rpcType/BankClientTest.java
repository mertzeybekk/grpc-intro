package com.mert.client.rpcType;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {
    private com.mert.models.BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub; //senkron çalışma sağlar
    private com.mert.models.BankServiceGrpc.BankServiceStub bankServiceStub; //asenkron çalışma sağlar

    @BeforeAll
    public void setUp() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        this.bankServiceBlockingStub = com.mert.models.BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = com.mert.models.BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void balanceTest() {
        com.mert.models.BalanceCheckRequest balanceCheckRequest =
                com.mert.models.BalanceCheckRequest.newBuilder()
                        .setAccountNumber(12)
                        .build();
        com.mert.models.Balance balance = this.bankServiceBlockingStub.getBalance(balanceCheckRequest);
        System.out.println("Received : " + balance.getAmount());
    }

    @Test
    public void withdrawTest() {
        com.mert.models.WithdrawRequest withdrawRequest = com.mert.models.WithdrawRequest.newBuilder()
                .setAccountNumber(5)
                .setAmount(40)
                .build();
        this.bankServiceBlockingStub.withdraw(withdrawRequest)
                .forEachRemaining(money -> System.out.println(money));
    }
    @Test
    public void withdrawAsyncTest(){
        com.mert.models.WithdrawRequest withdrawRequest = com.mert.models.WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();
        this.bankServiceStub.withdraw(withdrawRequest,new MoneyStreamingResponse());

        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        //System.out.println("dee");
    }
    @Test
    public void  cashStreamingRequest() throws InterruptedException {
        CountDownLatch latch  = new CountDownLatch(1);
        StreamObserver<com.mert.models.DepositRequest> depositRequestStreamObserver =
                this.bankServiceStub.cashDeposit(new BalanceStreamObserver(latch));
        for (int i = 0; i <10 ; i++) {
            com.mert.models.DepositRequest depositRequest = com.mert.models.DepositRequest.newBuilder()
                    .setAccountNumber(8)
                    .setAmount(10).build();
            depositRequestStreamObserver.onNext(depositRequest);
        }
        depositRequestStreamObserver.onCompleted();
        latch.await();
    }


}
