    Image getSplashImage() {
        URL url = this.getClass().getClassLoader().getResource("splash");
        LOG.debug(url);
        ArrayList<String> l = new ArrayList<String>();
        if (url.getProtocol().startsWith("jar")) {
            JarURLConnection conn = null;
            JarFile jarfile = null;
            try {
                conn = (JarURLConnection) url.openConnection();
                jarfile = conn.getJarFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Enumeration<JarEntry> e = jarfile.entries();
            while (e.hasMoreElements()) {
                String j = e.nextElement().getName();
                if (j.startsWith("splash") && j.endsWith(".png")) {
                    l.add(j);
                }
            }
            return new Image(null, this.getClass().getClassLoader().getResourceAsStream(l.get(new Random(new Date().getTime()).nextInt(l.size()))));
        } else {
            File dir = null;
            try {
                dir = new File(url.toURI());
                LOG.debug(url.toURI());
            } catch (URISyntaxException e) {
            }
            CollectionUtils.addAll(l, dir.list(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    LOG.debug(name);
                    return name.endsWith(".png");
                }
            }));
            return new Image(null, this.getClass().getClassLoader().getResourceAsStream("splash/" + l.get(new Random(new Date().getTime()).nextInt(l.size()))));
        }
    }
