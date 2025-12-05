package day5.part1;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    void main() throws URISyntaxException, IOException {
        var lines = Utils.loadLines("day5.txt");
        var freshRanges = lines.stream().takeWhile(line -> !line.isBlank()).map(l -> {
            var splitted = l.split("-");
            return new FreshRange(Long.parseLong(splitted[0]),Long.parseLong(splitted[1]));
        }).toList();
        var freshIds = lines
                .stream()
                .dropWhile(line -> !line.isBlank())
                .skip(1)
                .map(Long::parseLong)
                .filter(id -> freshRanges.stream().anyMatch(range -> range.contains(id)));
        System.out.println(freshIds.count());
    }
}

class FreshRange {
    long start;
    long end;

    FreshRange(long start, long end) {
        this.start = start;
        this.end = end;
    }

    boolean contains(long elem) {
        return start <= elem && end >= elem;
    }
}

