        private BufferedReader read(String url) throws Exception {
            return new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        }
