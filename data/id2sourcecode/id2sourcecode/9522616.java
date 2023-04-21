    public static String getStringFromStream(InputStream is) throws IOException {
        try {
            InputStreamReader reader = new InputStreamReader(is);
            char[] buffer = new char[1024];
            StringWriter writer = new StringWriter();
            int bytes_read;
            while ((bytes_read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, bytes_read);
            }
            return (writer.toString());
        } finally {
            if (null != is) is.close();
        }
    }
