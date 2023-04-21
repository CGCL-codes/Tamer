    public static TaskTable parse(URL url) throws IOException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            return parse(stream);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable t) {
                    ;
                }
            }
        }
    }
