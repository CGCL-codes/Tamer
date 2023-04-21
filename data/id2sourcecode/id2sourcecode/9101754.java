    private void initialize(URL url) {
        InputStream stream = null;
        Properties props = new Properties();
        try {
            try {
                stream = url.openStream();
                fileSize = stream.available();
                props.load(stream);
                for (Iterator iter = props.keySet().iterator(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    keySize += sizeOf(key);
                    valueSize += sizeOf(props.getProperty(key));
                    keyCount++;
                }
                hashSize = sizeOf(props);
            } finally {
                if (stream != null) stream.close();
            }
        } catch (IOException e) {
        }
    }
