    private SecurityApplication parseSecurityXMLFile(Digester d, FacesContext fctx) throws IOException, SAXException {
        URL url = null;
        ExternalContext exctx = fctx != null ? fctx.getExternalContext() : null;
        String _facesSecurityConfig = "security-config.xml";
        if (fctx.getCurrentInstance() != null) {
            String requestPathTranslated = ((HttpServletRequest) exctx.getRequest()).getPathTranslated();
            String requestPage = ((HttpServletRequest) exctx.getRequest()).getPathInfo();
            String requestPageOsTranslated = requestPage.replace('/', File.separatorChar);
            String public_html_location = requestPathTranslated.substring(0, requestPathTranslated.indexOf(requestPageOsTranslated));
            url = new URL("file:" + public_html_location + File.separator + "WEB-INF" + File.separator + _facesSecurityConfig);
            if (url == null) {
                url = getFacesSecurityConfigFromClasspath(_facesSecurityConfig);
            }
            if (url == null) {
                throw new FileNotFoundException("No encuentro el fichero de configuraci�n de seguridad: " + _facesSecurityConfig);
            }
        } else {
            url = getFacesSecurityConfigFromClasspath(_facesSecurityConfig);
        }
        return (SecurityApplication) d.parse(url.openStream());
    }
