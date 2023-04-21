    public HttpURLConnection openDecoratorConnection() throws IOException {
        URL url = new URL(baseUrl + "/__$$_decorateSampler");
        HttpURLConnection retVal = (HttpURLConnection) url.openConnection();
        retVal.setDoOutput(true);
        retVal.setDoInput(true);
        return retVal;
    }
