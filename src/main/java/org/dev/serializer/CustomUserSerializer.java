package org.dev.serializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.dev.entities.User;

public class CustomUserSerializer implements Serializer<User> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String s, User user) {
        try {
            if (user == null){
                System.out.println("Null received at serializing");
                return null;
            }
            return objectMapper.writeValueAsBytes(user);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing User to byte[]");
        }
    }
}
