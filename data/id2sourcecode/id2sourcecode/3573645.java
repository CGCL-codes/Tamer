    protected String readFromFile(String sFileName) {
        ByteArrayOutputStream bsOut = new ByteArrayOutputStream();
        FileInputStream fiIn = null;
        BufferedInputStream bIn = null;
        String sDataString = "";
        try {
            fiIn = new FileInputStream(sFileName);
            bIn = new BufferedInputStream(fiIn);
            int iData = 0;
            while ((iData = bIn.read()) > -1) bsOut.write(iData);
            sDataString = bsOut.toString();
            fiIn.close();
            bIn.close();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
        return sDataString;
    }
