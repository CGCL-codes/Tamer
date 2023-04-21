    static void tryLoad(Map<String, String> map, String uri) {
        URL url = Config.class.getClassLoader().getResource(uri);
        Properties props = new Properties();
        if (url != null) {
            InputStream is = null;
            try {
                is = url.openStream();
                props.load(is);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
        for (Object key : props.keySet()) {
            map.put((String) key, (String) props.get(key));
        }
    }
