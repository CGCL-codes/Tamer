package odk.lang;

public final class IntWrapper {

    public int value;

    public String toString() {
        return new StringBuilder().append(this.value).toString();
    }
}
