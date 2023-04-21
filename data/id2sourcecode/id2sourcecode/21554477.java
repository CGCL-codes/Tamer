    private void performRowSwap() {
        for (int row = 0; row < variables.length - 3; row += 3) {
            IntVar[] temp = variables[row];
            variables[row] = variables[row + 2];
            variables[row + 2] = temp;
        }
    }
