package day4.part1;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    void main() throws URISyntaxException, IOException {
        var spots = spotList();
        System.out.println(spots.stream().filter(s -> s.canBeAccessed() && s.hasPaperRoll).count());
    }

    private List<Spot> spotList() throws URISyntaxException, IOException {
        var lines = Utils.loadLines("day4.txt");
        var spots = lines.stream().map(line -> line.chars().mapToObj(character -> {
            var hasPaperRoll = character == '@';
            return new Spot(hasPaperRoll);
        }).toList()).toList();
        for (int y = 0; y < spots.size(); y++) {
            var row = spots.get(y);
            for (int x = 0; x < row.size(); x++) {
                var spot = row.get(x);
                var adjacentSpotAddresses = new int[][]{
                        {y - 1, x - 1},
                        {y - 1, x},
                        {y - 1, x + 1},
                        {y, x - 1},
                        {y, x + 1},
                        {y + 1, x - 1},
                        {y + 1, x},
                        {y + 1, x + 1},
                };
                for (int i = 0; i < adjacentSpotAddresses.length; i++) {
                    var spotAddress = adjacentSpotAddresses[i];
                    try {
                        spot.adjacentSpots[i] = spots.get(spotAddress[0]).get(spotAddress[1]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                }
            }
        }
        return spots.stream().flatMap(Collection::stream).toList();
    }
}
class Spot {
    Spot[] adjacentSpots = new Spot[8];
    boolean hasPaperRoll;
    Spot(boolean hasPaperRoll) {
       this.hasPaperRoll = hasPaperRoll;
    }

    boolean canBeAccessed() {
        return Stream.of(adjacentSpots).peek(s -> {
            if (s == null) {
                return;
            }
        }).filter(s ->s != null && s.hasPaperRoll).count() < 4;
    }
}

