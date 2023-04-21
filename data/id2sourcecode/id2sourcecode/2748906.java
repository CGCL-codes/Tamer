    private void _mindex() {
        int k = pop();
        int e = stack[stackIndex - k];
        for (int i = stackIndex - k; i < stackIndex - 1; i++) {
            stack[i] = stack[i + 1];
        }
        stack[stackIndex - 1] = e;
    }
