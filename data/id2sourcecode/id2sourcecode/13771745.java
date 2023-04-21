    public static <T> T readObject(Class<T> targetClass, String bindingName, URL url) throws JiBXException, IOException {
        InputStream in = url.openStream();
        try {
            return JiBXUtil.readObject(targetClass, bindingName, in);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
