    void openDataTypeFile() throws UnsupportedEncodingException, IOException {
        dataTypeFileName = "DataTypes.txt";
        try {
            dataTypeFile = new File(dataTypeFileName);
            dataTypeFis = new FileInputStream(dataTypeFile);
            dataTypeIsr = new InputStreamReader(dataTypeFis, "UTF-8");
        } catch (FileNotFoundException ex) {
            URL url = ClassLoader.getSystemResource(dataTypeFileName);
            dataTypeIs = url.openStream();
            dataTypeIsr = new InputStreamReader(dataTypeIs, "UTF-8");
        }
        dataTypeReader = new BufferedReader(dataTypeIsr);
    }
