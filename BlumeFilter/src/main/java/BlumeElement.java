import java.util.Objects;

public interface BlumeElement {
    HashFunction[] hashFamily();

    interface HashFunction {
        int hash();
    }

    abstract class AbstractElement implements BlumeElement {
        private Object object;

        public AbstractElement(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AbstractElement that = (AbstractElement) o;
            return Objects.equals(object, that.object);
        }

        @Override
        public int hashCode() {
            return Objects.hash(object);
        }

        @Override
        public String toString() {
            return object.toString();
        }
    }

    class ElementImpl extends AbstractElement {
        public ElementImpl(Object object) {
            super(object);
        }

        @Override
        public HashFunction[] hashFamily() {
            int[] h = new int[]{hashCode()};
            return new HashFunction[]{
                    () -> h[0] % 17 * 37 + h[0] / 3,
                    () -> h[0] % 37 + h[0] * h[0] / 64,
                    () -> h[0] % 2 + 32 * 17 * h[0] + 256,
            };
        }
    }

}
