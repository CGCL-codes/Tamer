    public static byte[] downloadURL(String InternetURL) {
        byte[] content = null;
        ByteArrayOutputStream output = null;
        URL url = null;
        URLConnection connection = null;
        InputStream is = null;
        try {
            url = new URL(InternetURL);
            connection = url.openConnection();
            output = new ByteArrayOutputStream();
            is = connection.getInputStream();
            int last = 0;
            while (last != -1) {
                last = is.read();
                output.write(last);
            }
            content = output.toByteArray();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
