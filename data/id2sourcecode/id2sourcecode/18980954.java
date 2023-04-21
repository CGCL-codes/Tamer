    @SuppressWarnings("unchecked")
    private void addJarsToClassLoader(GateClassLoader gcl, Element jdomElt) throws MalformedURLException {
        if ("JAR".equals(jdomElt.getName())) {
            URL url = new URL(creoleFileUrl, jdomElt.getTextTrim());
            try {
                java.io.InputStream s = url.openStream();
                s.close();
                gcl.addURL(url);
            } catch (IOException e) {
                Err.println("Error opening JAR " + url + " specified in creole file " + creoleFileUrl + ": " + e);
            }
        } else {
            for (Element child : (List<Element>) jdomElt.getChildren()) {
                addJarsToClassLoader(gcl, child);
            }
        }
    }
