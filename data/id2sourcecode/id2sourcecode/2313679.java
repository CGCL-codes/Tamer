        protected InputStream getFile(String filename) throws IOException {
            try {
                URL url = new URL(base, filename);
                URLConnection conn = url.openConnection();
                conn.setAllowUserInteraction(true);
                return conn.getInputStream();
            } catch (IOException ex) {
                return null;
            }
        }
