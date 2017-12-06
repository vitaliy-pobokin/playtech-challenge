package org.examples.pbk.playtech;

import javax.annotation.CheckForNull;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;
import static org.examples.pbk.playtech.UserEntry.USERNAME_PASSWORD_DELIMITER;

public class PasswordFile {
    private final Path file;
    private final Charset charset = Charset.forName("US-ASCII");
    private static final String FILENAME = ".passwords";

    public PasswordFile(String directory) {
        Path path = Paths.get(directory, FILENAME);
        try {
            if (Files.notExists(path)) Files.createFile(path);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        this.file = path;
    }

    public void writeUserEntry (UserEntry userEntry) {
        String stringToWrite = userEntry.getStringRepresentation();

        try (BufferedWriter writer = Files.newBufferedWriter(file, charset, WRITE, APPEND)) {
            writer.write(stringToWrite, 0, stringToWrite.length());
            writer.newLine();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    @CheckForNull
    public UserEntry findUserEntry(String username) {
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + USERNAME_PASSWORD_DELIMITER)) {
                    return UserEntry.valueOf(line);
                }
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return null;
    }
}
