    private void shiftToLeft(final Gene[] genes, final int begin, final int end) {
        for (int i = begin; i < end; i++) {
            genes[i] = genes[i + 1];
        }
    }
