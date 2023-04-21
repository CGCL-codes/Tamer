    public byte[] getMapFile(org.jdom.Document doc) {
        byte[] byx = new byte[100];
        try {
            String libraryId = doc.getRootElement().getChildTextTrim("LibraryID");
            String filename = doc.getRootElement().getChildTextTrim("FileName");
            String filepath = ejb.bprocess.util.NewGenLibRoot.getRoot() + java.util.ResourceBundle.getBundle("server").getString("Maps");
            filepath += "/LIB_" + libraryId + "/" + filename;
            java.io.File actualfile = new java.io.File(filepath);
            java.nio.channels.FileChannel fc = (new java.io.FileInputStream(actualfile)).getChannel();
            int fileLength = (int) fc.size();
            java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
            byx = new byte[bb.capacity()];
            bb.get(byx);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return byx;
    }
