package day1.part1;

import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;

public class Main {
    static void main() throws URISyntaxException, IOException {
        var knob = new Knob();
        var moves = loadMoves();
        for (var move : moves) {
            knob.move(move);
        }
        System.out.println(knob.zeroCount);
    }

    private static List<Move> loadMoves() throws URISyntaxException, IOException {
        var lines = Utils.loadLines("day1.txt");
        var linePattern = Pattern.compile("([LR])(\\d*)");
        return lines
                .stream()
                .map(line -> {
                    var matcher = linePattern.matcher(line);
                    if (matcher.matches()){
                        var directionStr = matcher.group(1);
                        var valueStr = matcher.group(2);
                        return new Move(directionStr.equals("R") ? Direction.R : Direction.L, Integer.parseInt(valueStr));
                    }
                    else throw new RuntimeException();
                }).toList();
    }
}

class Knob {
    int currentValue = 50;
    int zeroCount = 0;

    void move(Move move) {
        var newUnrestrictedValue = move.direction == Direction.L ? currentValue - move.value : currentValue + move.value;
        if (newUnrestrictedValue > 99) {
            currentValue = newUnrestrictedValue - 100;
        }
        else if (newUnrestrictedValue < 0) {
            currentValue = 100 + newUnrestrictedValue;
        }
        else currentValue = newUnrestrictedValue;
        if (currentValue == 0){
            zeroCount++;
        }
    }
}

class Move {
    Direction direction;
    int value;

    Move(Direction direction, int value) {
        this.direction = direction;
        this.value = value % 100;
    }
}

enum Direction {
    L, R
}
