    private String retrieveFileContent(String fileName) {
        try {
            fis = new FileInputStream(fileName);
            fis.close();
        } catch (FileNotFoundException fnf) {
            log.warn(DTD_USAGE_MESSAGE);
            saveFile = false;
            exit(new RuntimeException("scene \"" + fileName + "\" not found", fnf));
        } catch (IOException ioe) {
            log.fatal(ioe);
        }
        try {
            raf = new RandomAccessFile(fileName, "rwd");
            fc = raf.getChannel();
            bb = ByteBuffer.allocate((int) fc.size());
            fc.read(bb);
        } catch (IOException ioe) {
            readOnlyFile = true;
        }
        if (raf == null) {
            try {
                raf = new RandomAccessFile(fileName, "r");
                log.warn("Scene is read-only!");
                fc = raf.getChannel();
                bb = ByteBuffer.allocate((int) fc.size());
                fc.read(bb);
            } catch (IOException ioe) {
                saveFile = false;
                exit(new RuntimeException("unable to read scene \"" + fileName + "\"", ioe));
            }
        }
        bb.flip();
        String returnString = new String(bb.array());
        bb = null;
        return returnString;
    }
