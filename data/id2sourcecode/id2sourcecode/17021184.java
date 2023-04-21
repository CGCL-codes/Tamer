        private static InputStream getInputStream(URL url) throws IOException {
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            return new BufferedInputStream(conn.getInputStream());
        }
