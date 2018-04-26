package uk.co.handmadetools.hmm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Hmm {

    private Hmm() {}

    private int minLength = 6;
    private int maxLength = 12;

    private Map<String, Integer> firstLetterCounts = new HashMap<>();
    private Map<String, Integer> twoLetterCounts = new HashMap<>();
    private Map<String, Integer> threeLetterCounts = new HashMap<>();
    private Random random = new Random();

    public static Hmm fromSource(List<String> sources, int minLength, int maxLength) {
        Hmm hmm = new Hmm();
        hmm.minLength = minLength;
        hmm.maxLength = maxLength;
        hmm.init(sources);
        return hmm;
    }

    private void init(List<String> sources) {
        for (String source : sources) {
            if (source.length() < 5) {
                continue;
            }

            for (int i = 0; i < source.length(); i++) {
                if (i ==0) {
                    String key = source.substring(i, i + 1);
                    if (firstLetterCounts.containsKey(key)) {
                        firstLetterCounts.put(key, firstLetterCounts.get(key) + 1);
                    } else {
                        firstLetterCounts.put(key, 1);
                    }
                } else if (i == 1) {
                    String key = source.substring(i - 1, i + 1);
                    if (twoLetterCounts.containsKey(key)) {
                        twoLetterCounts.put(key, twoLetterCounts.get(key) + 1);
                    } else {
                        twoLetterCounts.put(key, 1);
                    }
                } else {
                    String key = source.substring(i - 2, i + 1);
                    if (threeLetterCounts.containsKey(key)) {
                        threeLetterCounts.put(key, threeLetterCounts.get(key) + 1);
                    } else {
                        threeLetterCounts.put(key, 1);
                    }
                }
            }
        }
//        System.out.println("Hmm init done");
//        System.out.println(firstLetterCounts);
//        System.out.println(twoLetterCounts);
//        System.out.println(threeLetterCounts);
    }

    public String generateName() {
//        System.out.println("generateName");
        while (true) {
            String name = generateAttempt();

            if (name.length() < minLength) {
                continue;
            }

            if (name.length() > maxLength) {
                continue;
            }

            // check triple characters
            if (countSequence(name) >= 2) {
                continue;
            }

            // TODO: check uniqueness

            String capitalized = "";
            for (int i = 0; i < name.length(); i++) {
                if (i == 0 || "' ".contains(name.substring(i - 1, i))) {
                    capitalized = capitalized + name.substring(i, i + 1).toUpperCase();
                } else {
                    capitalized = capitalized + name.substring(i, i + 1);
                }
            }
            return capitalized;
        }
    }

    private int countSequence(String name) {
        int maxCount = 0;
        String counting = "";
        int count = 0;
        for (int i = 0; i < name.length(); i++) {
            if (i == 0 || !name.substring(i, i + 1).equals(counting)) {
                counting = name.substring(i, i + 1);
                count = 1;
            } else {
                count++;
                if (count > maxCount) {
                    maxCount = count;
                }
            }
        }
        return maxCount;
    }

    private String generateAttempt() {
//        System.out.println("generateAttempt");

        String name = "";
        name = name + pickOne(firstLetterCounts, "");
        name = name + pickOne(twoLetterCounts, name);
        String s = pickOne(threeLetterCounts, name);
        while (!s.equals(".") && name.length() < maxLength) {
            name = name + s;
//            System.out.println(name);
            s = pickOne(threeLetterCounts, name.substring(name.length() - 2, name.length()));
        }
        return name;
    }

    private String pickOne(Map<String, Integer> counts, String start) {
        Map<String, Integer> filteredMap = counts.entrySet().stream()
                .filter(e -> e.getKey().startsWith(start))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        int total = filteredMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .mapToInt(i -> i).sum();
        int pick = random.nextInt(total);
        int runningCount = 0;
        for (Map.Entry<String, Integer> entry : filteredMap.entrySet()) {
            runningCount += entry.getValue();
            if (runningCount > pick) {
                String s = entry.getKey();
                return s.substring(s.length() - 1, s.length());
            }
        }
        throw new RuntimeException("pickOne failed");
    }

}
