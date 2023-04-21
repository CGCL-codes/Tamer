    private void open(final String theFilename) {
        try {
            final FileOutputStream myFileOutputStream;
            final BufferedOutputStream myBufferedOutputStream;
            if (USE_ZIP) {
                myFileOutputStream = new FileOutputStream(theFilename + ".zip");
                final ZipOutputStream myZip = new ZipOutputStream(myFileOutputStream);
                final String[] myFullPathName = theFilename.split("/");
                final String mySceneName = myFullPathName[myFullPathName.length - 1];
                myZip.putNextEntry(new ZipEntry(mySceneName));
                myBufferedOutputStream = new BufferedOutputStream(myZip);
            } else {
                myFileOutputStream = new FileOutputStream(theFilename);
                myBufferedOutputStream = new BufferedOutputStream(myFileOutputStream);
            }
            _myPrintStream = new PrintStream(myBufferedOutputStream);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
