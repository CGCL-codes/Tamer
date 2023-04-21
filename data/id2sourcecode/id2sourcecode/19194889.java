    protected byte[] loadClassBytes(String className) {
        className = formatClassName(className);
        try {
            URL url = new URL(urlString + className);
            URLConnection connection = url.openConnection();
            if (sourceMonitorOn) {
                print("Loading from URL: " + connection.getURL());
            }
            monitor("Content type is: " + connection.getContentType());
            InputStream inputStream = connection.getInputStream();
            int length = connection.getContentLength();
            monitor("InputStream length = " + length);
            byte[] data = new byte[length];
            inputStream.read(data);
            inputStream.close();
            return data;
        } catch (Exception ex) {
            print("### URLClassLoader.loadClassBytes() - Exception:");
            ex.printStackTrace();
            return null;
        }
    }
