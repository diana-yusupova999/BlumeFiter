import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Tests {

    @Test
    public void test(){
        BlumeElement[] right = new BlumeElement[]{
                new ElementImpl("gobgibjif"),
                new ElementImpl("lklmglbk"),
                new ElementImpl("gkb"),
                new ElementImpl("lkbmflkmb"),
                new ElementImpl("ilwsakxvxn"),
                new ElementImpl("svcmvmvmvm"),
                new ElementImpl("[ypotopyo]"),
                new ElementImpl("mvvnttiil"),
                new ElementImpl("kjklkghjoi"),
                new ElementImpl("iiyuurprii"),
                new ElementImpl("mmvnbb,"),
                new ElementImpl("lgklfkglkoobioyrlkmg.fm"),
                new ElementImpl(".,zxzvnoylilgknb"),
                new ElementImpl("glkbjoyle/tlbm"),
                new ElementImpl("l.c,vm.gkbm. "),
                new ElementImpl("kgk    lgkg "),
                new ElementImpl("fkbokv lg"),
                new ElementImpl("pogp 3409h "),
                new ElementImpl("44m ok olk3"),
                new ElementImpl("v kl.,.x,nck "),
                new ElementImpl("aswszx/lhp"),
                new ElementImpl("kfjon599bn xg"),
                new ElementImpl("03994086034-4"),
                new ElementImpl("p3 kjgn oigirgoi  gkkg"),
                new ElementImpl("fiv 04ti04"),
                new ElementImpl("otib04 050 i"),
                new ElementImpl("gkj kfj nv           bkvj kvj vj  vv j555555"),
                new ElementImpl("gobgibjif"),
        };
        BlumeElement[] wrong = new BlumeElement[]{
                new ElementImpl("ijiri334ii4i"),
                new ElementImpl("3rojkbkjfb f k b"),
                new ElementImpl("gkjogfof "),
                new ElementImpl("gffkk bkbk bk  b"),
                new ElementImpl(" kv v  km "),
                new ElementImpl("vv kf og k k"),
                new ElementImpl("[fk kfp0000]"),
                new ElementImpl("k, fk k pf kok jk "),
                new ElementImpl("vk fo ofirio409090 "),
                new ElementImpl("2pf-2vpkf k vl ok "),
                new ElementImpl("o rn54nojr uf oj ,"),
                new ElementImpl("o orn ogn og o k  lm km.fm"),
                new ElementImpl("., km mn  knw lkfvn i  "),
                new ElementImpl("gobgibji"),
                new ElementImpl("gobgibjif3")
        };
        int inputSize = right.length+wrong.length;
        BlumeElement[] testInputs = new BlumeElement[inputSize];
        for (int i = 0; i < inputSize; i++) {
            if (new Random().nextBoolean() && i < wrong.length) {
                testInputs[i] = wrong[i];
            } else if (i < right.length) {
                testInputs[i] = right[i];
            }
        }
        BlumeFilterSet<BlumeElement> filterSet = new BlumeFilterSet<>(10000000);
        filterSet.addAll(Arrays.asList(right));
        for (BlumeElement testInput : testInputs) {
            boolean isRight = false;
            for (BlumeElement element : right) {
                if (element == testInput) {
                    isRight = true;
                    break;
                }
            }
            Assertions.assertEquals(filterSet.contains(testInput), isRight);
        }
    }

    @Test
    public void testRemove() {
        ElementImpl element = new ElementImpl("qwerty");
        BlumeFilterSet<ElementImpl> blumeFilterSet = new BlumeFilterSet<>(1000000);
        blumeFilterSet.add(element);
        Assertions.assertTrue(blumeFilterSet.contains(element));
        blumeFilterSet.remove(element);
        Assertions.assertFalse(blumeFilterSet.contains(element));
    }

    private static class ElementImpl implements BlumeElement {
        String text;
        public ElementImpl(String text) {
            this.text = text;
        }
        @Override
        public int hashCode() {
            return Objects.hash(text);
        }
        @Override
        public String toString() {
            return text;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ElementImpl element = (ElementImpl) o;
            return Objects.equals(text, element.text);
        }
        @Override
        public HashFunction[] hash() {
            return new HashFunction[]{
                    ()->hashCode()*32,
                    ()->hashCode()*64,
                    ()->hashCode()*16
            };
        }
    }
}
