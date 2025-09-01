package ru.clouddonate.cloudpaymentslegacy.json;

import java.io.Reader;

public interface ConverterService {
    public String serialize(Object var1);

    public <T> T deserialize(Reader var1, Class<T> var2);
}
