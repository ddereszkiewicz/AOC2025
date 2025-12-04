package day3.part2;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Main {
    void main() throws URISyntaxException, IOException {
        var lines = Utils.loadLines("day3.txt");
        System.out.println(lines.stream().mapToLong(this::largestJoltage).sum());
    }

    long largestJoltage(String bank) {
        var joltage = new StringBuilder();
        var bankList = bank
                .chars()
                .mapToObj(Character::getNumericValue)
                .collect(Collectors.toCollection(ArrayList::new));
        Integer lastMax;
        while (joltage.length() < 12) {
            lastMax = bankList
                    .stream()
                    .limit(bankList.size() - (11 - joltage.length()))
                    .max(Comparator.comparingInt(i -> i))
                    .get();
            joltage.append(lastMax);
            var lastMaxIndex = bankList.indexOf(lastMax);
            if (lastMaxIndex + 1 != bankList.size()) bankList.subList(0, bankList.indexOf(lastMax) + 1).clear();
        }
        return Long.parseLong(joltage.toString());
    }

}

