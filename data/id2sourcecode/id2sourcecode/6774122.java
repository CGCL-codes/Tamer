    public void performTest(String packageName, String dataLocation, String resultLocation, String xslLocation) throws IOException, DFDLException {
        FileInputStream fis = null;
        FileChannel fic = null;
        try {
            try {
                URI uri = new URI(dataLocation);
                File f = new File(uri);
                fis = new FileInputStream(f);
            } catch (Exception e) {
                fis = new FileInputStream(dataLocation);
            }
            if (fis.available() > MAXINMEMORYSIZE) {
                fic = fis.getChannel();
                MappedByteBuffer bb = fic.map(FileChannel.MapMode.READ_ONLY, 0L, fis.available());
                GenericDFDLTest test = new GenericDFDLTest(bb, packageName, xslLocation, resultLocation);
                bb.clear();
                bb = null;
            } else {
                byte[] bb = new byte[fis.available()];
                fis.read(bb);
                GenericDFDLTest test = new GenericDFDLTest(bb, packageName, xslLocation, resultLocation);
            }
        } catch (Throwable e) {
            System.err.println("Exception caught while running defuddle translation.");
            e.printStackTrace();
            DFDLException de = new DFDLException();
            de.setStackTrace(e.getStackTrace());
            throw de;
        }
        try {
            if (fic != null) {
                fic.close();
            }
        } catch (IOException e) {
        }
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
        }
    }
