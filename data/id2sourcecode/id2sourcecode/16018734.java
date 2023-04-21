    private InputStream getStream() {
        InputStream result = null;
        try {
            Activator.log("Querying weather: " + url.toString());
            result = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
