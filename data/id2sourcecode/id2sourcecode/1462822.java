    public void registerWar(String alias, InputStream war, ClassLoader cl) throws ServletException, NamespaceException {
        if (alias == null || alias.length() == 0) {
            throw new NamespaceException("Invalid alias on registerWar: " + alias);
        }
        if (alias.length() > 0 && alias.charAt(alias.length() - 1) == '/') {
            alias = alias.substring(0, alias.length() - 1);
        }
        if (cMap.containsKey(alias)) {
            throw new NamespaceException("Alias already registered on registerWar: " + alias);
        }
        if (war == null) {
            throw new ServletException("Null war on registerWar");
        }
        String fname = alias + ".war";
        if (fname.lastIndexOf('/') != -1) {
            fname = fname.substring(fname.lastIndexOf('/'));
        }
        File tmp = new File(jstorage, fname);
        if (tmp.exists() && !tmp.delete()) {
            throw new ServletException("Temporary file exists and can't be deleted: " + tmp.getAbsolutePath());
        }
        byte[] buf = new byte[4096];
        try {
            FileOutputStream tf = new FileOutputStream(tmp);
            do {
                int read = war.read(buf, 0, buf.length);
                if (read == -1) {
                    break;
                }
                tf.write(buf, 0, read);
            } while (true);
            tf.close();
        } catch (IOException iox) {
            log.error("Error copying war file: " + iox.getMessage(), iox);
            tmp.delete();
            throw new ServletException("Cannot copy war file: " + iox.getMessage());
        }
        try {
            WebAppContext wac = new WebAppContext(tmp.getAbsolutePath(), alias);
            wac.setServer(hcoll.getServer());
            wac.start();
            cMap.put(alias, wac);
            hcoll.addHandler(wac);
        } catch (Exception ex) {
            throw new ServletException("Error deploying war file: " + ex.getMessage());
        }
        log.info("Registered war for " + alias);
    }
