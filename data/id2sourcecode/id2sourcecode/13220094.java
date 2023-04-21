        private static BufferedReader readerFor(URL url) throws IOException {
            return new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
        }
