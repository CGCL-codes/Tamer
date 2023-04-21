    public static InputStream getContentAsInputStream(URL url) throws SecurityException, IllegalArgumentException, IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }
        try {
            InputStream content = url.openStream();
            if (content == null) {
                throw new IllegalArgumentException("No content.");
            }
            return content;
        } catch (SecurityException e) {
            throw new SecurityException("Your JVM's SecurityManager has " + "disallowed this.");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("This file was not found: " + url);
        }
    }
