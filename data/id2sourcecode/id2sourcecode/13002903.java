    public static String validate(String urlStr) {
        URL url;
        if (urlStr == null) return getErrorMessage("URL is null"); else if (!urlStr.startsWith("http://")) return getErrorMessage("URL does not begin with 'http://'");
        try {
            url = new URL(urlStr);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("HEAD");
            httpConnection.connect();
            int response = httpConnection.getResponseCode();
            if (response >= 300) return getErrorMessage(response + " " + httpConnection.getResponseMessage());
        } catch (MalformedURLException mue) {
            return getErrorMessage("Malformed URL");
        } catch (IOException e) {
            return getErrorMessage("IO Exception when validating URL");
        }
        return "<success/>";
    }
