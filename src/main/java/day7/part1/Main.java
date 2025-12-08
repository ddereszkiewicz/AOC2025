package day7.part1;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        runBeam(layers);
        System.out.println(layers.stream().mapToLong(Layer::getSplitCount).sum());
    }

    void runBeam(List<Layer> layers) {
        List<Integer> currentBeams = List.of();
        for (var layer : layers) {
            currentBeams = layer.pass(currentBeams);
        }
    }
}

interface Layer {
    long getSplitCount();

    List<Integer> pass(List<Integer> beamsIndices);
}

class StartLayer implements Layer {
    List<String> layer;

    StartLayer(List<String> layer) {
        this.layer = layer;
    }

    @Override
    public long getSplitCount() {
        return 0;
    }

    @Override
    public List<Integer> pass(List<Integer> beamsIndices) {
        return List.of(this.layer.indexOf("S"));
    }
}

class SubsequentLayer implements Layer {
    List<String> layer;
    private long splitCount = 0;

    SubsequentLayer(List<String> layer) {
        this.layer = layer;
    }


    @Override
    public List<Integer> pass(List<Integer> beamsIndices) {
        var newBeams = new ArrayList<Integer>();
        for (var beamIndex : beamsIndices) {
            var beamHitSpot = layer.get(beamIndex);
            if (beamHitSpot.equals("^")) {
                newBeams.add(beamIndex - 1);
                newBeams.add(beamIndex + 1);
                splitCount += 1;
            } else {
                newBeams.add(beamIndex);
            }
        }
        return newBeams.stream().distinct().toList();
    }

    @Override
    public long getSplitCount() {
        return splitCount;
    }
}
