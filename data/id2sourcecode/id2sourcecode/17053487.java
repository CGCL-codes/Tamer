    public int append(BitOut bos, int last, int runShift) throws IOException {
        int current = runShift - 1;
        for (int i = 0; i < termDf; i++) {
            int docid = postingSource.readGamma() + current;
            bos.writeGamma(docid - last);
            bos.writeUnary(postingSource.readGamma());
            current = last = docid;
            final int numOfBlocks = postingSource.readUnary() - 1;
            bos.writeUnary(numOfBlocks + 1);
            if (numOfBlocks > 0) for (int j = 0; j < numOfBlocks; j++) {
                bos.writeGamma(postingSource.readGamma());
            }
        }
        try {
            postingSource.align();
        } catch (Exception e) {
        }
        return last;
    }
