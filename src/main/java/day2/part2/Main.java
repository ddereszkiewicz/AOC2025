package day2.part2;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main {
    static void main() throws URISyntaxException, IOException {
        var ranges = loadRanges();
        var invalidIdsSum = ranges.stream().mapToLong(IdRange::invalidIdsSum).sum();
        System.out.println(invalidIdsSum);
    }

    static List<IdRange> loadRanges() throws URISyntaxException, IOException {
        return Arrays.stream(Utils.loadLines("day2.txt").getFirst().split(",")).map(line -> {
                    var splitted = line.split("-");
                    return new IdRange(Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
                }
        ).toList();
    }
}

class IdRange {
    long start;
    long end;

    IdRange(long start, long end) {
        this.start = start;
        this.end = end;
    }

    long invalidIdsSum() {
        return LongStream.range(start, end + 1).filter(this::isInvalid).sum();
    }

    private boolean isInvalid(long id) {
        var asString = String.valueOf(id);
        return IntStream
                .range(1, asString.length() / 2 + 1)
                .mapToObj(i -> isInvalidRepeatedWithSubstringSize(asString, i))
                .anyMatch(b -> b);
    }

    private boolean isInvalidRepeatedWithSubstringSize(String id, int size) {
        if (id.length() % size != 0) return false;
        var parts = IntStream.range(0, id.length() / size).mapToObj(i -> id.substring(size * i, size * i + size)).toList();
        for (var part : parts) {
            if (!part.equals(parts.getFirst())) {
                return false;
            }
        }
        return true;
    }
}

