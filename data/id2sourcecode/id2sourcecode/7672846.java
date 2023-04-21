    public static InputStream downloadResource(URL codebaseURL, String name) {
        if (codebaseURL == null) return null;
        InputStream inputStream = null;
        try {
            String urlName = replaceSpaces(replaceSeparators(name));
            URL url = new URL(codebaseURL, urlName);
            System.out.println("Downloading \"" + name + "\" from\n  \"" + url + "\"");
            URLConnection urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();
            return inputStream;
        } catch (IOException ex) {
            try {
                inputStream.close();
            } catch (Exception ex1) {
            }
            return null;
        }
    }
