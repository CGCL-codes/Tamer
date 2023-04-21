        private BufferedReader createReaderForUrl(String fileAsUrl) throws MalformedURLException, IOException {
            URL url = new URL(fileAsUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            return reader;
        }
