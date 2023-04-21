    public static char[] readfile(String sFilePath, String sCharSet) throws FileNotFoundException, IOException, OutOfMemoryError, FTPException {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin FileSystem.readfile(" + sFilePath + ", charset=" + sCharSet + ")");
            DebugFile.incIdent();
        }
        char[] aBuffer;
        String sLower = sFilePath.toLowerCase();
        if (sLower.startsWith("file://")) sFilePath = sFilePath.substring(7);
        aBuffer = new FileSystem().readfilestr(sFilePath, sCharSet).toCharArray();
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End FileSystem.readfile() : " + String.valueOf(aBuffer.length));
        }
        return aBuffer;
    }
