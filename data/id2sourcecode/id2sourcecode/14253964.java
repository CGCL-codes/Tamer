    public void dupBnth1() {
        assert mutable;
        sp -= 1;
        Object tmp = stack[sp] = stack[sp + 1];
        stack[sp + 1] = stack[sp + 2];
        stack[sp + 2] = tmp;
    }
