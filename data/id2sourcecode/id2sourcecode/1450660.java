    @Before
    public void setUp() throws Exception {
        this.outputData = new String();
        int port = 8185;
        GlassFish glassfish = newGlassFish(port);
        URL url = new URL("http://xnova.test:" + port + "/" + WEB_CONTEXT + "/run-tests");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
        String line = null;
        while (null != (line = br.readLine())) {
            this.outputData = this.outputData.concat(line);
        }
        glassfish.stop();
    }
