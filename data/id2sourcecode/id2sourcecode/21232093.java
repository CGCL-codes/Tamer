    public static InputStream createHTMLInputStream(String urlString, String[][] argumentArray) throws MalformedURLException, IOException {
        if (argumentArray != null) {
            boolean urlHasArguments = (urlString.indexOf("?") > 0);
            for (String[] argument : argumentArray) {
                if (urlHasArguments) {
                    urlString += "&";
                } else {
                    urlString += "?";
                    urlHasArguments = true;
                }
                urlString += argument[0] + "=" + argument[1];
            }
        }
        return (new URL(urlString)).openConnection().getInputStream();
    }
