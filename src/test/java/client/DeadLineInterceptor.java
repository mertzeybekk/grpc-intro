package client;

import io.grpc.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DeadLineInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        Deadline  deadline = callOptions.getDeadline(); //İlk olarak, callOptions.getDeadline() ile mevcut bir zaman sınırı (deadline) alınmaya çalışılır.
        // Eğer isteğe zaten bir zaman sınırı tanımlanmışsa, bu zaman sınırı kullanılır
       // Eğer isteğe henüz bir zaman sınırı tanımlanmamışsa (deadline null ise)
        if(Objects.isNull(deadline)){
            callOptions=callOptions.withDeadline(Deadline.after(4,TimeUnit.SECONDS));
        }
        return channel.newCall(methodDescriptor,callOptions);
    }
}
