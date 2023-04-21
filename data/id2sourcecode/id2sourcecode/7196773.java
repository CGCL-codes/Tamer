    public static void addEndpoints(URL url) {
        Reader reader = null;
        Properties prop = new Properties();
        try {
            reader = new InputStreamReader(url.openStream());
            prop.load(reader);
        } catch (Throwable t) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable t) {
                }
            }
        }
        for (Map.Entry<Object, Object> entry : prop.entrySet()) {
            try {
                Class<?> cla = Class.forName((String) entry.getValue(), true, Thread.currentThread().getContextClassLoader());
                addEndpoint((String) entry.getKey(), (Endpoint) cla.newInstance());
            } catch (Throwable t) {
            }
        }
    }
