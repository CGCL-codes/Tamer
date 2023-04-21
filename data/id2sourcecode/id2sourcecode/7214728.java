    public boolean pipeFile(String fname) {
        try {
            URL url = new URL(server);
            conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            FileOutputStream tout = new FileOutputStream(fname);
            int nmax = 10000;
            byte b[] = new byte[nmax + 1];
            int nread = 0;
            while ((nread = is.read(b, 0, nmax)) >= 0) tout.write(b, 0, nread);
            return true;
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return false;
    }
