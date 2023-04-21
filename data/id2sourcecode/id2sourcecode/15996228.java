    public void open() throws FileNotFoundException {
        raf = new RandomAccessFile(srcFile, "r");
        chan = raf.getChannel();
        fsize = srcFile.length();
        byt2write = 0;
        written = 0;
        pos = 0;
        fpos = 0;
        isOpened = true;
        try {
            fillBuffer();
        } catch (Exception e) {
            isOpened = false;
            System.out.println("Error - unable to initialize buffer");
            if (debug) {
                e.printStackTrace();
            }
        }
    }
