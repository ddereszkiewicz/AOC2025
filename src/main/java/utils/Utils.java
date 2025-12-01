package utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utils {
    public static List<String> loadLines(String fileName) throws URISyntaxException, IOException {
        var path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        return Files.readAllLines(path);
    }
}
