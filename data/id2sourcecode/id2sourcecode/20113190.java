    protected void loadArchives() throws IOException, MalformedURLException {
        System.out.println("Loading... ");
        int size = 0;
        StringTokenizer stok = new StringTokenizer(getParameter("pa_archive"), ",");
        String[] jars = new String[stok.countTokens()];
        URL[] urls = new URL[stok.countTokens()];
        for (int i = 0; stok.hasMoreTokens(); i++) {
            jars[i] = stok.nextToken().trim();
            urls[i] = new URL(getCodeBase(), jars[i]);
            size += urls[i].openConnection().getContentLength();
            System.gc();
        }
        System.out.println("Overall size: " + size);
        int cnt = 0;
        int read;
        for (int i = 0; i < urls.length; i++) {
            InputStream is = urls[i].openConnection().getInputStream();
            byte buf[] = new byte[4096];
            while ((read = is.read(buf)) != -1) {
                cnt += read;
                setLine(cnt * 100 / size);
            }
            System.out.println("Loaded: " + jars[i]);
            is.close();
        }
        setLine(-1);
    }
