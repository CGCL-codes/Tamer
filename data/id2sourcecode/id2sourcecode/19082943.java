    void assertWritable() throws IllegalThreadStateException {
        if (readOnly) {
            throw new IllegalThreadStateException("You are attempting to make changes to " + this + " in a property change callback. This is illegal. You may only make module system changes while holding a write mutex and not inside a change callback. See #16328.");
        }
    }
