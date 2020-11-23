import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class Tests {
    private static final Set<BlumeElement> testInput = generateObjects(100);
    private static final Set<BlumeElement> testWrongInput = generateObjects(100, be -> !testInput.contains(be));

    @Test
    public void testClear() {
        BlumeFilterSet<BlumeElement> filterSet = initBlumeFilterSet(10, 1);
        filterSet.clear();
        Assertions.assertTrue(filterSet.isEmpty());
    }

    @Test
    public void testEquals() {
        BlumeFilterSet<BlumeElement> filterSet1 = new BlumeFilterSet<>(testInput, 100);
        BlumeFilterSet<BlumeElement> filterSet2 = new BlumeFilterSet<>(testInput, 100);
        BlumeFilterSet<BlumeElement> filterSet3 = initBlumeFilterSet(100, 100);
        Assertions.assertEquals(filterSet1, filterSet2);
        Assertions.assertNotEquals(filterSet1, filterSet3);
    }

    @Test
    public void testSize() {
        BlumeFilterSet<BlumeElement> filterSet = new BlumeFilterSet<>(100);
        Assertions.assertEquals(0, filterSet.size());
        filterSet.add(new BlumeElement.ElementImpl("element"));
        Assertions.assertEquals(1, filterSet.size());
        filterSet.add(new BlumeElement.ElementImpl("element2"));
        Assertions.assertEquals(2, filterSet.size());
    }

    @Test
    public void testIterator() {
        BlumeFilterSet<BlumeElement> filterSet = new BlumeFilterSet<>(testInput, 500);
        Iterator<BlumeElement> it = filterSet.iterator();
        ArrayList<BlumeElement> test = new ArrayList<>();
        while (it.hasNext()) {
            test.add(it.next());
        }
        Assertions.assertTrue(test.containsAll(testInput));
    }

    public void testAddContains(Set<BlumeElement> testInput, Set<BlumeElement> testWrongInput, int maxMembers, double error) {
        int rightInputSize = testInput.size();
        int wrongInputSize = testWrongInput.size();

        BlumeFilterSet<BlumeElement> filterSet = new BlumeFilterSet<>(maxMembers, error);

        for (BlumeElement element : testInput) {
            filterSet.add(element);
        }

        double rightContainsCounter = 0;
        double rightNotContainsCounter = 0;

        for (BlumeElement input : testInput) {
            if (filterSet.contains(input)) {
                rightContainsCounter++;
            }
        }
        for (BlumeElement input : testWrongInput) {
            if (!filterSet.contains(input)) {
                rightNotContainsCounter++;
            }
        }
        double rightContainsPercent = rightContainsCounter / rightInputSize * 100;
        double rightNotContainsPercent = rightNotContainsCounter / wrongInputSize * 100;
        double errorPercent = 100 - rightNotContainsPercent;
        double wrongNotContainsPercent = 100 - rightContainsPercent;
        Assertions.assertEquals(0, wrongNotContainsPercent);
        Assertions.assertTrue(errorPercent / 100 <= error*2);
    }

    @Test
    public void testAddContains() {
        int rightInputSize = 200;
        int wrongInputSize = 200;
        int maxMembers = 200;
        double error = 0.01;

        Set<BlumeElement> testInput = generateObjects(rightInputSize);
        Set<BlumeElement> testWrongInput = generateObjects(wrongInputSize, element -> !testInput.contains(element));

        testAddContains(testInput, testWrongInput, maxMembers, error);
    }

    @Test
    public void testAddContainsAll() {
        BlumeFilterSet<BlumeElement> filterSet = new BlumeFilterSet<>(100);
        filterSet.addAll(testInput);
        Assertions.assertTrue(filterSet.containsAll(testInput));
    }

    @Test
    public void testRemove() {
        BlumeElement element = new BlumeElement.ElementImpl("qwerty");
        BlumeFilterSet<BlumeElement> blumeFilterSet = new BlumeFilterSet<>(1000);
        blumeFilterSet.add(element);
        Assertions.assertTrue(blumeFilterSet.contains(element));
        blumeFilterSet.remove(element);
        Assertions.assertFalse(blumeFilterSet.contains(element));
    }

    @Test
    public void testRemoveAll() {
        BlumeFilterSet<BlumeElement> blumeFilterSet = new BlumeFilterSet<>(testInput, 100);
        Assertions.assertFalse(blumeFilterSet.isEmpty());
        blumeFilterSet.removeAll(testInput);
        Assertions.assertTrue(blumeFilterSet.isEmpty());
    }

    @Test
    public void testAddContainsWithTimeCheck() {
        Set<BlumeElement> input = generateObjects(1000);
        BlumeFilterSet<BlumeElement> filterSet = new BlumeFilterSet<>(1000, 0.01);
        ArrayList<BlumeElement> arrayList = new ArrayList<>(input);
        filterSet.addAll(input);

        long timer1 = System.currentTimeMillis();

        arrayList.containsAll(testInput);
        arrayList.containsAll(testWrongInput);
        timer1 = System.currentTimeMillis() - timer1;

        long timer2 = System.currentTimeMillis();

        filterSet.containsAll(testInput);
        filterSet.containsAll(testWrongInput);
        timer2 = System.currentTimeMillis() - timer2;

        Assertions.assertTrue(timer1 > timer2);
    }

    public static Set<BlumeElement> generateObjects(int size, Function<BlumeElement, Boolean> condition) {
        Set<BlumeElement> blumeElements = new HashSet<>();
        for (int i = 0; i < size; i++) {
            BlumeElement element = new BlumeElement.ElementImpl(generateString());
            if (condition.apply(element)) {
                blumeElements.add(element);
            }
        }
        return blumeElements;
    }

    public static Set<BlumeElement> generateObjects(int size) {
        return generateObjects(size, c -> true);
    }

    public static String generateString() {
        Random r = new Random();
        byte[] bytes = new byte[r.nextInt(1000000)];
        r.nextBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    BlumeFilterSet<BlumeElement> initBlumeFilterSet(int maxMembers, int members) {
        return new BlumeFilterSet<>(generateObjects(members, c -> true), maxMembers);
    }
}
