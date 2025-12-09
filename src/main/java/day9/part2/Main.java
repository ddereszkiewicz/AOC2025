package day9.part2;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    void main(String[] args) throws URISyntaxException, IOException {
        var redTiles = Utils.loadLines("day9.txt").stream().map(l -> {
            var splitted = l.split(",");
            return new Tile(Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
        }).toList();
        var greenTilesLines = greenTilesLines(redTiles);
        var vertcialTilesLines = greenTilesLines.stream().filter(pair -> pair.first().x() == pair.second().x()).toList();
        var allGreenTiles = allGreenTiles(vertcialTilesLines, vertcialTilesLines.stream().mapToLong(Pair::y2).max().getAsLong());
        var allPairs = redTiles.stream()
                               .flatMap(tile1 -> redTiles.stream()
                                                         .dropWhile(x -> !x.equals(tile1))
                                                         .skip(1)
                                                         .map(tile2 -> new Pair(tile1, tile2))
                               ).sorted(Comparator.comparingLong(Pair::rectSize).reversed()).toList();
        for (int i = 0; i < allPairs.size(); i++) {
            var pair = allPairs.get(i);
            if (pair.onAllGreenTiles(allGreenTiles)){
                System.out.println(pair.rectSize());
                break;
            }
        }
    }

    private Map<Long, ArrayList<Pair>> allGreenTiles(List<Pair> verticalLines, long yLimit) {
        var result = new HashMap<Long, ArrayList<Pair>>();
        for (int i = 0; i <= yLimit; i++) {
            var y = i;
            var verticalLinesInRow = verticalLines
                    .stream()
                    .filter(l -> y >= l.y1() && y <= l.y2())
                    .sorted(Comparator.comparingLong(Pair::x1)).toList();
            Tile curr = null;
            for (int j = 0; j < verticalLinesInRow.size(); j++) {
                var verticalLineInRow = verticalLinesInRow.get(j);
                if (curr == null) {
                    curr = new Tile(verticalLineInRow.x1(), y);
                } else {
                    if (verticalLineInRow.y2() == y || verticalLineInRow.y1() == y) {
                        if (verticalLineInRow.y1() < y) {
                            if (result.containsKey((long)y - 1)){
                                if (result.get((long)y - 1).stream().noneMatch(p -> p.x1() <= verticalLineInRow.x1() - 1 && p.x2() >= verticalLineInRow.x1() - 1)){
                                    continue;
                                }
                            }
                        }
                        else {
                            if (result.containsKey((long)y - 1)){
                                if (result.get((long)y - 1).stream().anyMatch(p -> p.x1() <= verticalLineInRow.x1() - 1 && p.x2() >= verticalLineInRow.x1() - 1)){
                                    continue;
                                }
                            }
                        }
                    } ;
                    var newPair = new Pair(curr, new Tile(verticalLineInRow.x1(), y));
                    if (result.containsKey((long) y)) {
                        result.get((long) y).add(newPair);
                    } else {
                        var newList = new ArrayList<Pair>();
                        newList.add(newPair);
                        result.put((long) y, newList);
                    }
                    curr = null;
                }
            }
        }
        return result;
    }

    private List<Pair> greenTilesLines(List<Tile> tiles) {
        var result = new ArrayList<Pair>();
        for (int i = 0; i < tiles.size() - 1; i++) {
            var currentTile = tiles.get(i);
            var nextTile = tiles.get(i + 1);
            result.add(new Pair(currentTile, nextTile));
        }
        result.add(new Pair(tiles.getLast(), tiles.getFirst()));
        return result;
    }
}

record Pair(Tile first, Tile second) {
    long rectSize() {
        var a = Math.abs(first.x() - second.x()) + 1;
        var b = Math.abs(first.y() - second.y()) + 1;
        return a * b;
    }

    long x1() {
        return Math.min(first().x(), second.x());
    }

    long x2() {
        return Math.max(first().x(), second.x());
    }

    long y1() {
        return Math.min(first().y(), second.y());
    }

    long y2() {
        return Math.max(first().y(), second.y());
    }

    boolean onAllGreenTiles(Map<Long, ArrayList<Pair>> greenTiles) {
        for (long i = y1(); i <= y2() ; i++) {
            var y = i;
            var tileOnThisLevel = greenTiles.get(y);
            if (tileOnThisLevel == null) return false;
            if (tileOnThisLevel.stream().noneMatch(gt -> gt.y1() == y && gt.x1() <= x1() && gt.x2() >= x2())){
                return false;
            };
        }
        return true;
    }
};

record Tile(long x, long y) {
}

