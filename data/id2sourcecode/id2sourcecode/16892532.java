    public static char[] readfile(String sFilePath) throws FileNotFoundException, IOException, OutOfMemoryError, FTPException {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin FileSystem.readfile(" + sFilePath + ")");
            DebugFile.incIdent();
        }
        char[] aBuffer;
        String sLower = sFilePath.toLowerCase();
        if (sLower.startsWith("file://")) sFilePath = sFilePath.substring(7);
        if (sLower.startsWith("http://") || sLower.startsWith("https://") || sLower.startsWith("ftp://")) {
            aBuffer = new FileSystem().readfilestr(sFilePath, "UTF-8").toCharArray();
        } else {
            File oFile = new File(sFilePath);
            aBuffer = new char[(int) oFile.length()];
            FileReader oReader = new FileReader(oFile);
            oReader.read(aBuffer);
            oReader.close();
            oReader = null;
            oFile = null;
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End FileSystem.readfile() : " + String.valueOf(aBuffer.length));
        }
        return aBuffer;
    }
