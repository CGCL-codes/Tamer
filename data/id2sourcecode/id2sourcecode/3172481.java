    public void removeIntArrayColumn(int[][] cons2, int start) {
        int length = getWidth();
        int cons2leng = cons2.length;
        for (int k = start; k < (cons2leng - 1); k++) {
            cons2[k] = cons2[k + 1];
        }
    }
