package day6.part1;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Main {
    void main() throws URISyntaxException, IOException {
        var lines = Utils.loadLines("day6.txt")
                .stream()
                .map(line ->
                        Arrays.stream(line.split("\\s+"))
                                .filter(n -> !n.isBlank())
                                .toList())
                .toList();
        var lineLength = lines.getFirst().size();
        long result = 0;
        for (int j = 0; j < lineLength; j++) {
            var operation = lines.getLast().get(j);
            long calculationResult = operation.equals("*") ? 1 : 0;
            for (int i = 0; i < lines.size() - 1; i++) {
                long value = Long.parseLong(lines.get(i).get(j));
                if (operation.equals("*")) {
                    calculationResult *= value;
                } else {
                    calculationResult += value;
                }
            }
            result += calculationResult;
        }
        System.out.println(result);
    }
}
