    public static int[] readFileTraceImpl(File file, int det) {
        FileInputStream fin;
        try {
            fin = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("File not found!");
        }
        DataInputStream din = new DataInputStream(fin);
        ArrayList<Integer> tempret = new ArrayList<Integer>();
        int tempCh = DATA_LAYOUT.getChannelCount();
        try {
            din.skip(2 * det);
        } catch (IOException ex) {
            Logger.getLogger(NNJDataSourceBin464ii.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (din.available() != 0) {
                tempret.add(new Integer(din.readShort()));
                din.skip(2 * tempCh);
            }
        } catch (IOException ex) {
            Logger.getLogger(NNJDataSourceBin464ii.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            din.close();
        } catch (IOException ex) {
            Logger.getLogger(NNJDataSourceBin464ii.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fin.close();
        } catch (IOException ex) {
            Logger.getLogger(NNJDataSourceBin464ii.class.getName()).log(Level.SEVERE, null, ex);
        }
        return K.toInteger(tempret.toArray());
    }
