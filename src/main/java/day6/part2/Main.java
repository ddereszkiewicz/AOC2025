package day6.part2;

import utils.Utils;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    void main() throws URISyntaxException, IOException {
        var lines = Utils.loadLines("day6.txt")
                .stream()
                .map(line ->
                        Arrays.stream(line.split(""))
                                .toList())
                .toList();
        var columns = transpose(lines);
        var splittedColumns = splitByEmptyColumn(columns);
        var calculated = splittedColumns.stream().mapToLong(this::calculate);
        System.out.println(calculated.sum());
    }

    private long calculate(List<List<String>> calculation) {
        var operation = calculation.getFirst().getLast();
        var numbers = calculation
                .stream()
                .map(column -> column.stream()
                        .limit(column.size() - 1)
                        .reduce("", (acc, digit) -> {
                    if (digit.equals(" ")) return acc;
                    else return acc + digit;
                })).mapToLong(Long::parseLong);
        return Objects.equals(operation, "+") ? numbers.sum() : numbers.reduce((acc,next) -> acc * next).getAsLong();
    }

    private List<List<List<String>>> splitByEmptyColumn(List<List<String>> columns) {
        var result = new ArrayList<List<List<String>>>();
        var group = new ArrayList<List<String>>();
        for (var column : columns) {
            if (column.stream().allMatch(String::isBlank)) {
                result.add(group);
                group = new ArrayList<>();
                continue;
            }
            group.add(column);
        }
        result.add(group);
        return result;
    }

    List<List<String>> transpose(List<List<String>> input) {
        var result = new ArrayList<List<String>>();
        var lineLength = input.getFirst().size();
        for (int j = 0; j < lineLength; j++) {
            var column = new ArrayList<String>();
            for (List<String> strings : input) {
                column.add(strings.get(j));
            }
            result.add(column);
        }
        return result;
    }
}



