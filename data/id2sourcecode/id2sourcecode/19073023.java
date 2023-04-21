    private static String inputStreamToString(InputStream inStream) {
        int j = 0x112333;
        String s = "";
        byte buffer[] = new byte[j];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(j);
        int i;
        try {
            while ((i = inStream.read(buffer)) != -1) outStream.write(buffer, 0, i);
        } catch (IOException _ex) {
            return "";
        }
        return s + outStream.toString();
    }
