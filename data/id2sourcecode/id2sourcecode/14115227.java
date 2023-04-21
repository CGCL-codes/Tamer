        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            int len = _urls.length;
            URLConnection[] conn = new URLConnection[len];
            for (int i = 0; i < len; i++) {
                conn[i] = _urls[i].openConnection();
            }
            return new AggregatingURLConnection(u, conn, _separator);
        }
