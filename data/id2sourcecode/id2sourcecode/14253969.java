    public void swap() {
        assert mutable;
        Object tmp = stack[sp];
        stack[sp] = stack[sp + 1];
        stack[sp + 1] = tmp;
    }
