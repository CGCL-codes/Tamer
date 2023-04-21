    public String getFileContent(String fileName) {
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException fnf) {
            addLogEntryLine(errorToken + " [X3dDoctypeChecker] scene \"" + fileName + "\" not found.");
            addLogEntryLine(UsageMessage);
            saveFile = false;
            return "";
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
            addLogEntryLine(warningToken + " [X3dDoctypeChecker] " + fileName + " file is read-only.");
        } catch (IOException ioe) {
            addLogEntryLine(errorToken + " [X3dDoctypeChecker] unable to read scene \"" + fileName + "\".");
            System.out.println(outputLog);
            ioe.printStackTrace();
            return "";
        }
        String returnString = new String(bb.array());
        bb = null;
        return returnString;
    }
