    public static String getStringFromStream(InputStream is) {
        if (null == is) return null;
        try {
            InputStreamReader reader = new InputStreamReader(is);
            char[] buffer = new char[1024];
            StringWriter writer = new StringWriter();
            int bytes_read;
            while ((bytes_read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, bytes_read);
            }
            return (writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
