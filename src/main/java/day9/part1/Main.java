package day9.part1;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    void main() throws URISyntaxException, IOException {
        var redTiles = Utils.loadLines("day9.txt").stream().map(l -> {
            var splitted = l.split(",");
            return new RedTile(Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
        }).toList();
        var allPairs = redTiles.stream()
                               .flatMap(tile1 -> redTiles.stream()
                                                         .dropWhile(x -> !x.equals(tile1))
                                                         .skip(1)
                                                         .map(tile2 -> new Pair(tile1, tile2))
                               );
        System.out.println(allPairs.mapToLong(Pair::rectSize).max().getAsLong());
    }

}

record Pair(RedTile first, RedTile second) {
    long rectSize() {
        var a = Math.abs(first.x() - second.x()) + 1;
        var b = Math.abs(first.y() - second.y()) + 1;
        return a * b;
    }
};

record RedTile(long x, long y) {
}
