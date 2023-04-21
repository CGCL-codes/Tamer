    public boolean ping() {
        try {
            URL url = new URL("http://localhost:" + port + "/?ping");
            url.openStream();
        } catch (MalformedURLException e) {
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
