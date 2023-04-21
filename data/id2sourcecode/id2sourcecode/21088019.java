    public void loadTempDataStringFromFile(String sFilename) {
        try {
            ByteArrayOutputStream bsOut = new ByteArrayOutputStream();
            FileInputStream fiIn = new FileInputStream(sFilename);
            int iData = 0;
            while ((iData = fiIn.read()) > -1) bsOut.write(iData);
            String sDataString = bsOut.toString();
            fiIn.close();
            DataString = sDataString;
        } catch (IOException e) {
            DataString = "";
        }
    }
