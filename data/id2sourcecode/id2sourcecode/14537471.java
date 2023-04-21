    protected HttpURLConnection openHttpConnection(String toUrl) throws CheckoutException {
        URL url;
        try {
            url = new URL(toUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new CheckoutException(e);
        }
    }
