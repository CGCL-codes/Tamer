    public static Reader getCsvUrlReader(String url) throws DataSourceException {
        Reader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(new URL(url).openStream(), "UTF-8"));
        } catch (MalformedURLException e) {
            throw new DataSourceException(ReasonType.INVALID_REQUEST, "url is malformed: " + url);
        } catch (IOException e) {
            throw new DataSourceException(ReasonType.INVALID_REQUEST, "Couldn't read csv file from url: " + url);
        }
        return reader;
    }
