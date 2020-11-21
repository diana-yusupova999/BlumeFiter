import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class Tests {

    @Test
    public void testClear() {
        BlumeFilterSet<BlumeElement> filterSet = initBlumeFilterSet(10, 1);
        filterSet.clear();
        Assertions.assertTrue(filterSet.isEmpty());
    }

    @Test
    public void testEquals() {
        Set<BlumeElement> elements = generateObjects(100);
        BlumeFilterSet<BlumeElement> filterSet1 = new BlumeFilterSet<>(elements, 100);
        BlumeFilterSet<BlumeElement> filterSet2 = new BlumeFilterSet<>(elements, 100);
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
        Set<BlumeElement> elements = generateObjects(100);
        BlumeFilterSet<BlumeElement> filterSet = new BlumeFilterSet<>(elements, 1000);
        Iterator it = filterSet.iterator();
        ArrayList<BlumeElement> test = new ArrayList<>();
        while (it.hasNext()) {
            test.add((BlumeElement) it.next());
        }
        Assertions.assertTrue(test.containsAll(elements));
    }

    @Test
    public void testAddContains() {
        int rightInputSize = 100;
        int wrongInputSize = 100;
        int maxMembers = 1000;
        double error = 0.01;

        Set<BlumeElement> testSet = generateObjects(rightInputSize, be -> true);
        Set<BlumeElement> wrongSet = generateObjects(wrongInputSize, be -> !testSet.contains(be));

        BlumeFilterSet<BlumeElement> filterSet = new BlumeFilterSet<>(maxMembers, error);

        for (BlumeElement element : testSet) {
            filterSet.add(element);
        }

        double wrongContainsCounter = 0;
        double wrongNotContainsCounter = 0;
        double rightContainsCounter = 0;
        double rightNotContainsCounter = 0;

        for (BlumeElement input : testSet) {
            if (filterSet.contains(input)) {
                rightContainsCounter++;
            } else {
                wrongNotContainsCounter++;
            }
        }
        for (BlumeElement input : wrongSet) {
            if (filterSet.contains(input)) {
                wrongContainsCounter++;
            } else {
                rightNotContainsCounter++;
            }
        }

        System.out.println("rightContainsCounter = " + rightContainsCounter);
        System.out.println("rightNotContainsCounter = " + rightNotContainsCounter);
        System.out.println("wrongContainsCounter = " + wrongContainsCounter);
        System.out.println("wrongNotContainsCounter = " + wrongNotContainsCounter);
        System.out.println();
        double rightContainsPersent = rightContainsCounter / rightInputSize * 100;
        double rightNotContainsPersent = rightNotContainsCounter / wrongInputSize * 100;
        double wrongContainsPersent = 100 - rightNotContainsPersent;
        double wrongNotContainsPersent = 100 - rightContainsPersent;
        System.out.println("rightContainsPersent = " + rightContainsPersent + "%");
        System.out.println("rightNotContainsPersent = " + rightNotContainsPersent + "%");
        System.out.println("wrongContainsPersent = " + wrongContainsPersent + "%");
        System.out.println("wrongNotContainsPersent = " + wrongNotContainsPersent + "%");

        Assertions.assertTrue(wrongContainsPersent <= 2);
    }

    @Test
    public void testAddContainsAll() {
        Set<BlumeElement> elements = generateObjects(100);
        BlumeFilterSet<BlumeElement> filterSet = new BlumeFilterSet<>(100);
        filterSet.addAll(elements);
        Assertions.assertTrue(filterSet.containsAll(elements));
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
        Set<BlumeElement> elements = generateObjects(100);
        BlumeFilterSet<BlumeElement> blumeFilterSet = new BlumeFilterSet<>(elements, 100);
        Assertions.assertFalse(blumeFilterSet.isEmpty());
        blumeFilterSet.removeAll(elements);
        Assertions.assertTrue(blumeFilterSet.isEmpty());
    }

    public Set<BlumeElement> generateObjects(int size, Function<BlumeElement, Boolean> condition) {
        Set<BlumeElement> blumeElements = new HashSet<>();
        for (int i = 0; i < size; i++) {
            BlumeElement element = new BlumeElement.ElementImpl(generateString());
            if (condition.apply(element)) {
                blumeElements.add(element);
            }
        }
        return blumeElements;
    }

    public Set<BlumeElement> generateObjects(int size) {
        return generateObjects(size, c -> true);
    }

    BlumeFilterSet<BlumeElement> initBlumeFilterSet(int maxMembers, int members) {
        return new BlumeFilterSet<>(generateObjects(members, c -> true), maxMembers);
    }

    public String generateString() {
        Random r = new Random();
        byte[] bytes = new byte[r.nextInt(1000000)];
        r.nextBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
