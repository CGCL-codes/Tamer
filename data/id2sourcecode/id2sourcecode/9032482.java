        public void run() {
            try {
                URLConnection con = url.openConnection();
                InputStream stream = con.getInputStream();
            } catch (IOException e) {
                exception = e;
            }
        }
