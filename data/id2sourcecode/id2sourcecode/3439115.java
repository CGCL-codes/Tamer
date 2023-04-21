    public int append(BitOut bos, int last, int runShift) throws IOException {
        int current = runShift - 1;
        for (int i = 0; i < termDf; i++) {
            final int docid = postingSource.readGamma() + current;
            bos.writeGamma(docid - last);
            bos.writeUnary(postingSource.readGamma());
            current = last = docid;
        }
        try {
            postingSource.align();
        } catch (Exception e) {
        }
        return last;
    }
