    public static void main(String[] args) {
        initStreams();
        String vyberiSim = "AT+CPBS={SM}\r";
        String dayVseNaSim = "AT+CPBR=1,250\r";
        try {
            output.write(vyberiSim.getBytes());
            readAll();
            output.write(dayVseNaSim.getBytes());
            readAll();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        port.close();
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream("dump.bin", false);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        try {
            fs.write(readBuf, 0, readCount);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        try {
            fs.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
