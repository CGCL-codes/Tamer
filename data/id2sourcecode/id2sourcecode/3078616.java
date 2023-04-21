    protected void doSetup() throws Exception {
        final URL url = new URL(hudsonApp.getUrl() + (hudsonApp.getUrl().endsWith("/") ? "" : "/") + "createItem?name=" + hudsonApp.getJobName(infrastructureCfg.getModule()));
        final URLConnection con = url.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/xml");
        final OutputStream os = con.getOutputStream();
        final String configXml = "<project>\n" + "  <builders/>\n" + "  <publishers class=\"vector\"/>\n" + "  <buildWrappers class=\"vector\"/>\n" + getVCXml() + "  <canRoam>true</canRoam>\n" + "  <disabled>false</disabled>\n" + "  <triggers class=\"vector\"/>\n" + "  <keepDependencies>false</keepDependencies>\n" + "  <properties/>\n" + "  <description></description>\n" + "  <actions class=\"java.util.concurrent.CopyOnWriteArrayList\"/>\n" + "</project>";
        IOUtils.write(configXml, os);
        os.close();
        final InputStream in = con.getInputStream();
        logInfo(IOUtils.toString(in));
        in.close();
    }
