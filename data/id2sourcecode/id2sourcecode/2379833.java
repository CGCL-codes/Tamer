    public static String readContents(URL url) throws IOException {
        InputStream inputStream = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer contentsBuffer = new StringBuffer();
        try {
            String value;
            while ((value = reader.readLine()) != null) {
                contentsBuffer.append(value);
                contentsBuffer.append("\n");
            }
        } finally {
            reader.close();
            inputStream.close();
        }
        return contentsBuffer.toString();
    }
