package day2.part1;

import org.w3c.dom.ranges.Range;
import utils.Utils;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main {
    static void main() throws URISyntaxException, IOException {
        var ranges = loadRanges();
        var invalidIds =  ranges.stream().mapToLong(IdRange::invalidIdsSum).sum();
        System.out.println(invalidIds);
    }
    static List<IdRange> loadRanges() throws URISyntaxException, IOException {
       return Arrays.stream(Utils.loadLines("day2.txt").getFirst().split(",")).map(line -> {
                   var splitted = line.split("-");
                   return new IdRange(Long.parseLong(splitted[0]),Long.parseLong(splitted[1]));
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
        var halfAString = asString.length() / 2;
        return asString.substring(0, halfAString).equals(asString.substring(halfAString  ));
    }
}

