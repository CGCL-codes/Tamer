    protected FileChannel openFile(String name) {
        String filename = homeDir + File.separatorChar + "maxdocid";
        RandomAccessFile maxDocIdRaf;
        try {
            maxDocIdRaf = new RandomAccessFile(new File(filename), "rw");
            return maxDocIdRaf.getChannel();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
