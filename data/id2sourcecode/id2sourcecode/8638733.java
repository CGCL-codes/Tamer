    public <T> T execute(HttpURLConnectionCallback<T> action) throws HttpException {
        Assert.notNull(action, "HttpURLConnectionCallback object must not be null");
        try {
            return action.doInConnection((HttpURLConnection) new URL(url).openConnection());
        } catch (MalformedURLException e) {
            throw new HttpException(url, e);
        } catch (IOException e) {
            throw new HttpException(url, e);
        }
    }
