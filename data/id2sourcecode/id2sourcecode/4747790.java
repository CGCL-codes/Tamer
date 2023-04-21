    public void loadDataStringFromFile(String sFilename, boolean clearCurrentData, String sEncoding) {
        try {
            ByteArrayOutputStream bsOut = new ByteArrayOutputStream();
            FileInputStream fiIn = new FileInputStream(sFilename);
            int iData = 0;
            while ((iData = fiIn.read()) > -1) bsOut.write(iData);
            String sDataString = bsOut.toString();
            setDataString(sDataString, SourceNGramSize, clearCurrentData);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            setDataString("", 1, false);
        }
    }
