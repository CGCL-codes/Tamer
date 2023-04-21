    protected void initEffect(URL url) {
        initEffectCommon();
        try {
            String xmlText = PApplet.join(PApplet.loadStrings(url.openStream()), "\n");
            XMLElement xml = new XMLElement(xmlText);
            loadXML(xml);
        } catch (IOException e) {
            System.err.println("Error loading effect: " + e.getMessage());
        }
        initShader(url.toString(), true);
    }
