package day8.part1;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    void main() throws URISyntaxException, IOException {
        var boxes = Utils.loadLines("day8.txt").stream().map(
                line -> {
                    var splitted = line.split(",");
                    return new JunctionBox(
                            Integer.parseInt(splitted[0]),
                            Integer.parseInt(splitted[1]),
                            Integer.parseInt(splitted[2])
                    );
                }
        ).toList();
        var closestPairs = closestPairs(boxes);
        var circuits = collectIntoCircuits(closestPairs);
        System.out.println(closestPairs);
        System.out.println(
                circuits
                        .stream()
                        .map(HashSet::size)
                        .sorted(Comparator.comparingInt(i -> -i))
                        .limit(3)
                        .reduce((a, b) -> a * b)
                        .get()
        );
    }

    List<HashSet<JunctionBox>> collectIntoCircuits(List<Pair> pairs) {
        var circuits = new ArrayList<HashSet<JunctionBox>>();
        for (var pair : pairs) {
            var firstCircuit = circuits.stream().filter(s -> s.contains(pair.first())).findFirst();
            var secondCircuit = circuits.stream().filter(s -> s.contains(pair.second())).findFirst();
            if (firstCircuit.isEmpty() && secondCircuit.isEmpty()) {
                var newCircuit = new HashSet<JunctionBox>();
                newCircuit.add(pair.first());
                newCircuit.add(pair.second());
                circuits.add(newCircuit);
            } else if (firstCircuit.isEmpty()) {
                secondCircuit.get().add(pair.first());
            } else if (secondCircuit.isEmpty())  {
                firstCircuit.get().add(pair.second());
            } else {
               circuits.remove(firstCircuit.get());
               circuits.remove(secondCircuit.get());
               var merged = Stream.concat(firstCircuit.get().stream(), secondCircuit.get().stream()).collect(Collectors.toCollection(HashSet::new));
               circuits.add(merged);
            }
        }
        return circuits;
    }

    List<Pair> closestPairs(List<JunctionBox> boxes) {
        return boxes
                .stream()
                .flatMap(box1 -> boxes.stream().dropWhile(x -> !x.equals(box1)).skip(1).map(box2 -> new Pair(box1, box2)))
                .sorted(Comparator.comparingDouble(Pair::distance)).limit(1000)
                .toList();
    }
}

record JunctionBox(int x, int y, int z) {
};

record Pair(JunctionBox first, JunctionBox second) {
    double distance() {
        return Math.sqrt(Math.pow(first.x() - second.x(), 2) +
                Math.pow(first.y() - second.y(), 2) +
                Math.pow(first.z() - second.z(), 2));
    }
};
