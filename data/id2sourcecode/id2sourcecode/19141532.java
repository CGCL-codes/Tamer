        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(url_.openStream()));
        }
