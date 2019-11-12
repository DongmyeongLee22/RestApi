package me.sun.restapi.account;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * ErrorsSerializer처럼 @JsonComponent를 붙이면
 * 모든 Account가 id값만 반환하므로 직접사용할 곳에 annotation을 붙인다.
 */
public class AccountSerializer extends JsonSerializer<Account> {
    @Override
    public void serialize(Account account, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", account.getId());
        jsonGenerator.writeEndObject();
    }
}
