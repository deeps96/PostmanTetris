package de.deeps.postman.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.*;

/**
 * Helper to read and write JSON-Files into objects using the JAVAX api.
 */
public class JsonConverter {

    @Getter(AccessLevel.PRIVATE) private static final ObjectMapper mapper = new ObjectMapper();

    private JsonConverter() {
        throw new IllegalStateException();
    }

    public static <T> T jsonFileToObject(String fileName, Class<T> classType) throws IOException {
        return jsonInputStreamToObject(new FileInputStream(fileName), classType);
    }

    public static <T> T jsonInputStreamToObject(String fileName,
                                                Class<T> classType) {
        try {
            return jsonInputStreamToObject(getInputStream(fileName, classType), classType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    public static <T> T jsonInputStreamToObject(InputStream inputStream,
                                                Class<T> classType) throws IOException {
        return getMapper().readValue(inputStream, classType);
    }

    private static <T> InputStream getInputStream(String filePath, Class<T> classType)
            throws FileNotFoundException {
        InputStream inputStream;
        if (new File(filePath).exists()) {
            inputStream = new FileInputStream(filePath);
        } else {
            inputStream = classType.getClassLoader()
                    .getResourceAsStream(filePath);
        }
        return inputStream;
    }

    public static void writeObjectToJsonFile(Object input, String filePath) throws IOException {
        writeObjectToJsonFile(input, new File(filePath));
    }

    private static void writeObjectToJsonFile(Object input, File file) throws IOException {
        getMapper().writerWithDefaultPrettyPrinter().writeValue(file, input);
    }

}
