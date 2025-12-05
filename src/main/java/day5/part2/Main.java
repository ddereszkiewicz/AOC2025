package day5.part2;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    void main() throws URISyntaxException, IOException {
        var lines = Utils.loadLines("day5.txt");
        var freshRanges = lines.stream().takeWhile(line -> !line.isBlank()).map(l -> {
            var splitted = l.split("-");
            return new FreshRange(Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
        }).toList();
        var squashed = squashRanges(freshRanges);
        System.out.println(squashed.stream().mapToLong(FreshRange::freshElementsCount).sum());
    }

    List<FreshRange> squashRanges(List<FreshRange> ranges) {
        var squashedRanges = new ArrayList<FreshRange>();
        for (var range : ranges) {
            var overlappingRanges = squashedRanges
                    .stream()
                    .filter(r -> r.overlaps(range)).toList();
            for (var overlappingRange : overlappingRanges) {
                squashedRanges.remove(overlappingRange);
            }
            squashedRanges.add(merge(Stream.concat(overlappingRanges.stream(), Stream.of(range)).toList()));
        }
        return squashedRanges;
    }

    FreshRange merge(List<FreshRange> ranges) {
        var start = ranges.stream().map(r -> r.start).min(Comparator.comparingLong(l -> l)).get();
        var end = ranges.stream().map(r -> r.end).max(Comparator.comparingLong(l -> l)).get();
        return new FreshRange(start, end);
    }
}

class FreshRange {
    long start;
    long end;

    FreshRange(long start, long end) {
        this.start = start;
        this.end = end;
    }

    long freshElementsCount() {
        return end - start + 1;
    }

    boolean overlaps(FreshRange another) {
        return another.start >= this.start && another.start <= this.end ||
                another.end >= this.start && another.end <= this.end ||
                another.start <= this.start && another.end >= this.end;
    }
}


