    private URL createURLFromWARFile() throws Throwable {
        URL url = this.servletContext.getResource("/WEB-INF/web-beans.xml");
        if (url != null) {
            WEBBEANS_XML_LOCATIONS.put(url.getFile(), url.openStream());
            return WarUrlFinder.findWebInfClassesPath(this.servletContext);
        }
        return null;
    }
