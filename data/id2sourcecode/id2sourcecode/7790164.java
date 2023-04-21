    private String loadFile(String sFilename) {
        try {
            ByteArrayOutputStream bsOut = new ByteArrayOutputStream();
            FileInputStream fiIn = new FileInputStream(sFilename);
            int iData = 0;
            while ((iData = fiIn.read()) > -1) bsOut.write(iData);
            String sDataString = bsOut.toString();
            fiIn.close();
            return sDataString;
        } catch (IOException e) {
            return "";
        }
    }
