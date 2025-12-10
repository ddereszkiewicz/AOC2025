package day4.part2;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    void main() throws URISyntaxException, IOException {
        var spots = spotList();
        long removedCount = 0;
        while (true) {
            spots.forEach(Spot::tryMarkForRemoval);
            spots.forEach(Spot::tryRemove);
            var newRemovedCount = spots.stream().filter(s -> s.removed).count();
            if (newRemovedCount == removedCount) break;
            else removedCount = newRemovedCount;
        }
        System.out.println(removedCount);
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
    boolean removed;
    boolean markedForRemoval;

    Spot(boolean hasPaperRoll) {
       this.hasPaperRoll = hasPaperRoll;
    }

    void tryMarkForRemoval(){
        if (canBeAccessed() && hasPaperRoll) markedForRemoval = true;
    }

    public void tryRemove() {
        if (markedForRemoval) remove();
    }

    private boolean canBeAccessed() {
        return Stream.of(adjacentSpots).filter(s -> s != null && s.hasPaperRoll).count() < 4;
    }

    private void remove() {
        hasPaperRoll = false;
        removed = true;
    }
}

