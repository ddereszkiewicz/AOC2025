package day3.part1;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    void main() throws URISyntaxException, IOException {
        var lines = Utils.loadLines("day3.txt");
        System.out.println(lines.stream().mapToInt(this::largestJoltage).sum());
    }

    int largestJoltage(String bank) {
        var bankList = bank
                .chars()
                .mapToObj(Character::getNumericValue)
                .toList();
        var max = bankList
                .stream()
                .limit(bank.length() - 1)
                .max(Comparator.comparingInt(i -> i));
        var firstMax = max.get();
        var secondMax = bankList
                .stream()
                .dropWhile(n -> !n.equals(firstMax))
                .skip(1)
                .max(Comparator.comparingInt(i -> i))
                .get();
        return Integer.parseInt(String.valueOf(firstMax) + String.valueOf(secondMax));
    }

}

