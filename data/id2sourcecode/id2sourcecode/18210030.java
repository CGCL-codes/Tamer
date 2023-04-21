    public void removeIntArrayColumn(int[][] cons2, int start) {
        int length = maxLength();
        int cons2leng = cons2.length;
        System.out.println("cons2 length " + cons2leng + " " + length);
        for (int k = start; k < (cons2leng - 1); k++) {
            cons2[k] = cons2[k + 1];
        }
    }
