    public void init() {
        try {
            String version = System.getProperty("java.specification.version");
            if (version != null) {
                if ((10.0 * Double.parseDouble(version)) < 15.0) {
                    throw new RuntimeException("Bad JAVA version : found \"" + version + "\" but expected >= 1.5.\nSee http://java.sun.com/j2se/1.5.0/download.jsp for more informations");
                }
            }
            String s = getParameter("foaf");
            if (s == null) throw new NullPointerException("parameter missing");
            URL url = new URL(getDocumentBase(), s);
            showStatus("Loading " + url.toString() + "... Please WAIT");
            InputStream in = url.openStream();
            this.model = Model.load(in);
            this.main = new Main(this.model) {

                private static final long serialVersionUID = 1L;

                public void activateURL(URL url) {
                    getAppletContext().showDocument(url, "" + System.currentTimeMillis());
                }
            };
            setContentPane(main);
        } catch (Throwable e) {
            setContentPane(new ExceptionPane(e));
        }
    }
