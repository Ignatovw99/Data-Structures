import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<String> strings = Arrays.stream(reader.readLine()
                .split(", "))
                .collect(Collectors.toList());
        String requiredString = reader.readLine();

        Wordcruncher wordcruncher = new Wordcruncher(requiredString, strings);
        System.out.println(String.join("", wordcruncher.formRequiredString()).trim());
    }
}
