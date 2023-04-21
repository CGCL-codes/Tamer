    private Properties readProperties(URL url) {
        try {
            InputStream is = url.openStream();
            Properties loaded = new Properties();
            loaded.load(is);
            Properties result = new Properties();
            for (Object key : loaded.keySet()) {
                String k = (String) key;
                String value = loaded.getProperty(k);
                if (value != null) {
                    value = value.trim();
                    try {
                        byte[] isoBytes = value.getBytes("ISO-8859-1");
                        String unicodeString = new String(isoBytes, "UTF-8");
                        result.put(k, unicodeString);
                    } catch (UnsupportedEncodingException e) {
                        logger.warn("Failed to convert text to unicode: key " + k + " value: " + value + ", err=" + e);
                        continue;
                    }
                }
            }
            is.close();
            return result;
        } catch (IOException e) {
            logger.warn("Failed to load property file: " + url + ", err=" + e);
            return null;
        }
    }
