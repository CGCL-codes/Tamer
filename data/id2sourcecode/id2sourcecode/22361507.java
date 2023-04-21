    @Override
    public Document getData() throws Exception {
        URLConnection urlConnection = null;
        try {
            urlConnection = super.getURL().openConnection();
            urlConnection.connect();
            return (Document) urlConnection.getContent();
        } finally {
            if (urlConnection != null) ((FileURLConnection) urlConnection).close();
        }
    }
