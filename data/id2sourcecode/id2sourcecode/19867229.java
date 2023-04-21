    public void makeCatalinaBase() throws Exception {
        getLog().debug("make catalina base for embedded Tomcat");
        if ((this.getWebXml() != null) && this.getWebXml().exists()) {
            this.getLog().info("source web.xml present - " + this.getWebXml() + " - using it with embedded Tomcat");
        } else {
            this.getLog().info("source web.xml NOT present, using default empty web.xml for shell");
        }
        String[] args = { this.getTomcat().getAbsolutePath(), this.getWebXml().getAbsolutePath(), this.getShellServletMappingURL() };
        MakeCatalinaBase.main(args);
        if ((this.getContextXml() != null) && this.getContextXml().exists()) {
            this.getLog().info("contextXml parameter present - " + this.getContextXml() + " - using it for embedded Tomcat ROOT.xml");
            FileUtils.copyFile(this.getContextXml(), new File(this.getTomcat(), "conf/gwt/localhost/ROOT.xml"));
        }
    }
