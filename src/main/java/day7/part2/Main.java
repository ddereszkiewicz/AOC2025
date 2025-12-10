package day7.part2;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    void main() throws URISyntaxException, IOException {
        var lines = Utils.loadLines("day7.txt");
        var layers = Stream.concat(
                Stream.of(new StartLayer(Arrays.stream(lines.getFirst().split("")).toList())),
                lines.stream()
                     .skip(1)
                     .map(l -> Arrays.stream(l.split("")).toList())
                     .map(SubsequentLayer::new)
        ).toList();
        var result = runBeam(layers);
        System.out.println(result.values().stream().mapToLong(l -> l).sum());
    }

    Map<Integer,Long> runBeam(List<Layer> layers) {
        Map<Integer, Long> currentBeams = Map.of();
        for (var layer : layers) {
            currentBeams = layer.pass(currentBeams);
        }
        return currentBeams;
    }
}

interface Layer {

    Map<Integer, Long> pass(Map<Integer, Long> beamsIndices);
}

class StartLayer implements Layer {
    List<String> layer;

    StartLayer(List<String> layer) {
        this.layer = layer;
    }

    @Override
    public Map<Integer,Long> pass(Map<Integer,Long> beamsIndices) {
        return Map.of(this.layer.indexOf("S"), 1L);
    }
}

class SubsequentLayer implements Layer {
    List<String> layer;

    SubsequentLayer(List<String> layer) {
        this.layer = layer;
    }


    @Override
    public Map<Integer, Long> pass(Map<Integer, Long> beamsTimelines) {
        var newTimelines = new HashMap<Integer,Long>();
        for (var entry : beamsTimelines.entrySet()) {
            var index = entry.getKey();
            var timelinesLeadingToThis = entry.getValue();
            var beamHitSpot = layer.get(index);
            if (beamHitSpot.equals("^")) {
                if(newTimelines.containsKey(index - 1)) {
                    newTimelines.compute(index - 1, (x, value) -> value + timelinesLeadingToThis);
                }
                else newTimelines.put(index - 1, timelinesLeadingToThis);
                if(newTimelines.containsKey(index + 1)) {
                    newTimelines.compute(index + 1, (x, value) -> value + timelinesLeadingToThis);
                }
                else newTimelines.put(index + 1, timelinesLeadingToThis);
            }
            else {
                if(newTimelines.containsKey(index)) {
                    newTimelines.compute(index, (x, value) -> value + timelinesLeadingToThis);
                }
                else newTimelines.put(index, timelinesLeadingToThis);
            }
        }
        return newTimelines;
    }

}
