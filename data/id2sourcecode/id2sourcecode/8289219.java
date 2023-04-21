        public void open() throws IOException {
            URL url = getLocator().getURL();
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            in = conn.getInputStream();
            tellPoint = 0;
        }
