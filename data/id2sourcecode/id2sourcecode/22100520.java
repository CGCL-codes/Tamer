    private void cycle() {
        for (int i = 0; i < DEPTH - 1; i++) {
            edits[i] = edits[i + 1];
            editTypes[i] = editTypes[i + 1];
        }
        currentEdit = DEPTH - 1;
    }
