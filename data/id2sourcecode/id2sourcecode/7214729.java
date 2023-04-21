    public String pipe(String ext) {
        String nfile = null;
        try {
            URL url = new URL(server);
            conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            File tfile = File.createTempFile(Jarnal.jarnalTmp, ext);
            nfile = tfile.getPath();
            FileOutputStream tout = new FileOutputStream(tfile);
            int nmax = 10000;
            byte b[] = new byte[nmax + 1];
            int nread = 0;
            while ((nread = is.read(b, 0, nmax)) >= 0) tout.write(b, 0, nread);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return nfile;
    }
