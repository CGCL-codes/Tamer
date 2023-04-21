    public boolean exists(String fileName) throws IOException {
        log.debug("does exist: " + fileName);
        URL url = new URL(this.endpointURL + "?operation=exists&filename=" + fileName);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        int found = 0;
        InputStream in = null;
        try {
            in = connection.getInputStream();
            found = in.read();
        } finally {
            if (in != null) in.close();
        }
        return (1 == found);
    }
