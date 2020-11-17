
public interface BlumeElement {
    HashFunction[] hash();
    interface HashFunction{
        int hash();
    }
}
