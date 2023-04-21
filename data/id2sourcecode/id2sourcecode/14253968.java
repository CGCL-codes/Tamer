    public void dup2Bnth2() {
        assert mutable;
        sp += 2;
        Object tmp1 = stack[sp] = stack[sp + 2];
        Object tmp2 = stack[sp + 1] = stack[sp + 3];
        stack[sp + 2] = stack[sp + 4];
        stack[sp + 3] = stack[sp + 5];
        stack[sp + 4] = tmp1;
        stack[sp + 5] = tmp2;
    }
