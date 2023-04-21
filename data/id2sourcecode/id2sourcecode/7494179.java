    private static RAFFile createLocalRAFFile(URL url) throws IOException {
        RAFFile result = null;
        URLConnection connection = url.openConnection();
        try {
            Class types[] = {};
            Method m = URLConnection.class.getMethod("getPermission", types);
            result = RAFFileFactoryOn12.get(connection);
        } catch (NoSuchMethodError ex) {
        } catch (NoSuchMethodException ex) {
        }
        if (result == null) {
            result = new MemoryRAFFile(connection);
            debug("Opening a Dict file in Memory");
        }
        return result;
    }
