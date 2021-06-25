package ru.levelp.at.hw4.task2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CollectionsApplication {

    private static final SortedSet<Integer> dividers = new TreeSet<>(Set.of(2, 3, 5, 7));

    public static void main(String[] args) {
        CollectionsApplication application = new CollectionsApplication();
        application.start();
    }

    public void start() {
        List<Integer> hundredThousandNumbers = IntStream.rangeClosed(1, 100000).boxed().collect(Collectors.toList());
        System.out.println("1-100000: ");
        System.out.println(hundredThousandNumbers);

        List<Integer> hundredRandomNumbers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            hundredRandomNumbers.add(new Random().nextInt(90));
        }
        System.out.println("Random numbers: ");
        System.out.println(hundredRandomNumbers);
        System.out.println("Elements repeat: ");
        System.out.println(hundredRandomNumbers.size() > new HashSet<>(hundredRandomNumbers).size());

        Map<Integer, List<Integer>> dividedNumbers
            = hundredRandomNumbers.stream().distinct().collect(Collectors.groupingBy(this::groupByDivider));
        System.out.println("Dividers map: ");
        System.out.println(dividedNumbers);
    }

    private Integer groupByDivider(Integer number) {
        for (Integer divider : dividers) {
            if (number % divider == 0) {
                return divider;
            }
        }
        return 1;
    }

}
