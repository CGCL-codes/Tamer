    public void addCpg(String contig, long pos, byte base, int baseQ, char strand, double methyValue, String readID) {
        String readsLine = String.format("%s\t%d\t%c\t%d\t%c\t%.2f\t%s\n", contig, pos, base, baseQ, strand, methyValue, readID);
        try {
            mWriter.write(readsLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
