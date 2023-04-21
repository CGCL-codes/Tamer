        XmlResourceBundle(ResourceBundle parent, URL url) {
            this.parent = parent;
            this.url = url;
            try {
                properties.loadFromXML(url.openStream());
            } catch (Exception ex) {
                log.error("on load properties from " + url);
            }
        }
