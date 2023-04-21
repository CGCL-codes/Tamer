    public Reader createReader(LocatorIF locator) throws IOException {
        URL url = new URL(locator.getAddress());
        return new InputStreamReader(url.openConnection().getInputStream());
    }
