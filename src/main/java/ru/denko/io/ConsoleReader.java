package ru.denko.io;

import java.util.Scanner;
import java.util.function.Consumer;
import lombok.Data;

@Data
public class ConsoleReader {

    private final Scanner scanner = new Scanner(System.in);

    public String readLine() {
        return scanner.nextLine();
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public void read(Consumer<String> consumer) {
        while (hasNextLine()) {
            consumer.accept(readLine());
        }
        scanner.close();
    }

}
