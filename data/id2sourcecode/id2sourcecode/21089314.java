    public static String getFileContent(String fileName) {
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException fnf) {
            System.out.println("[Error] [X3dDtdChecker] scene \"" + fileName + "\" not found.");
            System.out.println(UsageMessage);
            saveFile = false;
            exit(0);
        }
        try {
            raf = new RandomAccessFile(fileName, "rwd");
            fc = raf.getChannel();
            bb = ByteBuffer.allocate((int) fc.size());
            fc.read(bb);
            bb.flip();
        } catch (IOException ioe) {
            readOnlyFile = true;
        }
        if (raf == null) try {
            raf = new RandomAccessFile(fileName, "r");
            fc = raf.getChannel();
            bb = ByteBuffer.allocate((int) fc.size());
            fc.read(bb);
            bb.flip();
            System.out.println("[Warning] [X3dDtdChecker] ' + fileName + ' file is read-only.");
        } catch (IOException ioe) {
            System.out.println("[Error] [X3dDtdChecker] unable to read scene \"" + fileName + "\".");
            ioe.printStackTrace();
            System.out.println();
            saveFile = false;
            exit(0);
        }
        String returnString = new String(bb.array());
        bb = null;
        return returnString;
    }
