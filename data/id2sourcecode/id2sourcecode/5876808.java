        @Override
        public void init(HttpMethod meth, String url) throws IOException {
            this.conn = (HttpURLConnection) new URL(url).openConnection();
            this.conn.setRequestMethod(meth.name());
        }
