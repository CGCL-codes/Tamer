    private BufferedInputStream getDownloadStream(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            return new BufferedInputStream(stream);
        } catch (MalformedURLException e) {
            throw new Error("URL '" + urlString + "' is not valid: " + e.toString(), e);
        } catch (IOException e) {
            throw new Error("Error connecting to '" + urlString + "': " + e.toString(), e);
        } catch (SecurityException e) {
            throw new Error("Security Manager has denied connection to '" + urlString + "': " + e.toString(), e);
        }
    }
