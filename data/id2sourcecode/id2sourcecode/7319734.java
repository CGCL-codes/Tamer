    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        String resourcePath = (String) systemIdMap.get(systemId);
        if (resourcePath != null) {
            InputStream resourceStream = ResourceUtil.getResourceStream(resourcePath);
            if (systemId.equals("xhtml-lat1.ent") || systemId.equals("xhtml-symbol.ent") || systemId.equals("xhtml-special.ent")) {
                System.err.println("A problem in XML parser detected: external XML entity URLs are not resolved");
                System.err.println("\tPlease configure your runtime environment to use a different XML parser");
                System.err.println("\t(e.g. using javax.xml.parsers.SAXParserFactory system property)");
            }
            InputSource source = new InputSource(resourceStream);
            source.setPublicId(publicId);
            source.setSystemId(systemId);
            return source;
        } else if (systemId.startsWith(zipRoot)) {
            String rname = systemId.substring(zipRoot.length());
            if (!ocf.hasEntry(rname)) throw new SAXException("Could not resolve local XML entity '" + rname + "'");
            if (!ocf.canDecrypt(rname)) throw new SAXException("Could not decrypt local XML entity '" + rname + "'");
            InputStream resourceStream = ocf.getInputStream(rname);
            InputSource source = new InputSource(resourceStream);
            source.setPublicId(publicId);
            source.setSystemId(systemId);
            return source;
        } else {
            report.warning(resource, 0, "Unresolved external XML entity '" + systemId + "'");
            InputStream urlStream = new URL(systemId).openStream();
            InputSource source = new InputSource(urlStream);
            source.setPublicId(publicId);
            source.setSystemId(systemId);
            return source;
        }
    }
