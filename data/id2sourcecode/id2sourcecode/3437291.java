    @Override
    public Document getData() throws FileNotFoundException, ConnectException, IOException {
        if (this.resourceData != null) return this.resourceData;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) super.getURL().openConnection();
            urlConnection.setConnectTimeout(30000);
            urlConnection.connect();
            if (HttpURLConnection.HTTP_NOT_FOUND == urlConnection.getResponseCode()) {
                throw new FileNotFoundException("The resource " + super.getURL().toString() + " couldn't be found.");
            }
            if (HttpURLConnection.HTTP_OK != urlConnection.getResponseCode()) {
                throw new ConnectException("Can't connect to " + super.getURL().toString() + "\nStatus code: " + urlConnection.getResponseCode());
            }
            this.resourceData = (Document) urlConnection.getContent();
            return this.resourceData;
        } finally {
            if (urlConnection != null) ((HttpURLConnection) urlConnection).disconnect();
        }
    }
