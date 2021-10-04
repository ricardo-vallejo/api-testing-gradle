import java.io.IOException;
import java.util.Optional;

public class ConfVariables {

    public static String getHost() throws IOException {
        return Optional.ofNullable(System.getenv("host")).orElse((String) ApplicationProperties.getInstance().get("host"));
    }

    public static String getPath() throws IOException {
        return Optional.ofNullable(System.getenv("path")).orElse((String) ApplicationProperties.getInstance().get("path"));
    }
}
