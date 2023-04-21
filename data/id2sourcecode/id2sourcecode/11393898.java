    private void badMode() {
        throw Py.ValueError("Must have exactly one of read/write/append mode");
    }
