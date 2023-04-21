    public Main(String dir, String[] argv) throws IOException, SAXException {
        super(dir, argv);
        InputStream in;
        if (this.hasMainArg(0)) {
            String arg = this.getMainArg(0);
            try {
                URL url = new URL(arg);
                in = url.openStream();
            } catch (MalformedURLException exc) {
                File file = new File(arg);
                if (file.exists() && file.isFile()) in = new FileInputStream(file); else {
                    in = this.getResourceAsStream(arg);
                    if (null == in) in = this.getResourceAsStream(Resource);
                }
            }
        } else {
            in = this.getResourceAsStream(Resource);
        }
        if (null != in) {
            Jnlp jnlp;
            try {
                jnlp = new Jnlp("loader:" + Main.Resource, in);
                Main.LoadTestDebug();
            } finally {
                in.close();
            }
            this.base = jnlp.getCodebaseUrl();
            if (null == dir) {
                dir = jnlp.getHrefBase();
                this.temp = new File(dir);
                this.cache = new File(this.temp, "cache");
            }
            if ((!this.temp.exists() && (!this.temp.mkdirs()))) throw new IllegalStateException("Unable to create directory '" + this.temp.getAbsolutePath() + "'."); else if ((!this.cache.exists() && (!this.cache.mkdirs()))) throw new IllegalStateException("Unable to create directory '" + this.cache.getAbsolutePath() + "'."); else {
                if (this.clean) {
                    Clean cleaner = this.cleanTemp();
                    cleaner.run();
                    if (this.cleanOnly) {
                        this.jnlp = null;
                        return;
                    } else this.jnlp = jnlp;
                } else this.jnlp = jnlp;
                jnlp.copyMain(this.getResourceAsStream(Resource));
            }
        } else throw new IllegalStateException("Resource not found, '" + Resource + "'.");
    }
