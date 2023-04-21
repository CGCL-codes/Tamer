    @Override
    public void add(genomeObject obj) {
        String readsLine = String.format("%s\t%d\t%s\t%s\t%c\t%d\t%c\t%d\n", obj.getChr(), obj.getStart(), ((NOMeSeqReads) obj).getRefContext(), ((NOMeSeqReads) obj).getSampleContext(), ((NOMeSeqReads) obj).getMethyStatus(), ((NOMeSeqReads) obj).getbaseQ(), ((NOMeSeqReads) obj).getstrand(), ((NOMeSeqReads) obj).getEncryptID());
        try {
            mWriter.write(readsLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
