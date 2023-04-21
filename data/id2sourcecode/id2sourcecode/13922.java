    private void testWriteRead(byte[] byteData, String stringData, String charset) throws IOException {
        String byteFile = "bytefile.log";
        String stringFile = "stringfile.log";
        byte[] bresult;
        String sresult;
        Files.writeBytes(null, byteFile, byteData, false, false);
        Files.write(null, stringFile, stringData, charset, false, false);
        bresult = Files.readBytes(byteFile);
        if (Arrays.equals(byteData, bresult) == false) Log.error("byte write/byte read " + byteData.length);
        sresult = Files.read(byteFile, charset);
        if (sresult.compareTo(stringData) != 0) Log.error("byte write/string read " + byteData.length + "  " + sresult);
        bresult = Files.readBytes(stringFile);
        if (Arrays.equals(byteData, bresult) == false) Log.error("string write/byte read " + byteData.length);
        sresult = Files.read(stringFile, charset);
        if (sresult.compareTo(stringData) != 0) Log.error("string write/string read " + byteData.length);
    }
