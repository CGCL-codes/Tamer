    public TreeModel getDataSetList() throws org.das2.DasException {
        String formData = "server=list";
        try {
            URL server = new URL("http", host, port, path + "?" + formData);
            logger.log(Level.INFO, "connecting to {0}", server);
            URLConnection urlConnection = server.openConnection();
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            TreeModel result = createModel(in);
            logger.log(Level.INFO, "response->{0}", result);
            return result;
        } catch (IOException e) {
            throw new DasIOException(e.getMessage());
        }
    }
