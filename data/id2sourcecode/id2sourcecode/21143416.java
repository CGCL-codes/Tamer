    public void add(String contig, long pos, byte base, int baseQ, char strand, String readID) {
        String readsLine = String.format("%s\t%d\t%c\t%d\t%c\t%s\n", contig, pos, base, baseQ, strand, readID);
        try {
            mWriter.write(readsLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
