package com.mert.server.metaData;

import io.grpc.*;

import java.util.Objects;

public class AuthInterceptor implements ServerInterceptor {
/*
interceptCall metodu, bir gRPC sunucu çağrısı geldiğinde çağrılır. Bu metot, sunucu tarafındaki gelen isteği ve bu isteği işleyecek olan ServerCallHandler'ı alır.

İlk olarak, gelen isteği doğrulamak amacıyla sunucuya gelen isteği taşıyan metadata'dan bir clientToken alınır. metadata genellikle isteği taşıyan HTTP başlıklarını içerir.

validate adlı özel bir metot kullanılarak, gelen clientToken'ın geçerli olup olmadığı kontrol edilir. Eğer clientToken, "bank-client-secret" değerine eşitse ve null değilse, istek yetkilendirilir ve sunucu işlemine devam edilir. Aksi halde, istek reddedilir.

Yetkilendirme işlemi başarılı ise, serverCallHandler.startCall(serverCall, metadata) kullanılarak sunucu işlemi başlatılır ve istek ilgili sunucu yöntemine iletilir. İstek sunucu tarafında işlenir ve yanıtlar üretilir.

Eğer yetkilendirme başarısız olursa (validate metodu false dönerse), null döndürülür ve istek reddedilir. Sunucu tarafında hiçbir işlem gerçekleştirilmez.
 */

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
      // String clientToken = metadata.get(ServerConstants.TOKEN);
        String userToken = metadata.get(ServerConstants.USER_TOKEN);
       if(this.validate(userToken)){
          return serverCallHandler.startCall(serverCall,metadata);
       }else {
          Status status= Status.UNAUTHENTICATED.withDescription("invalid token / expired token");
          serverCall.close(status,metadata);
       }
        return new ServerCall.Listener<ReqT>() {
        };
    }
    private boolean validate(String token) {
        return Objects.nonNull(token) && token.equals("user-secret-3");
    }
}
