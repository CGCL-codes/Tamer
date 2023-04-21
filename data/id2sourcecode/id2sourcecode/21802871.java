    public void load() {
        if (loaded) return;
        loaded = true;
        final String propertyPath = VitaPad.getResources().getPropertyPath();
        URL url = VPResources.class.getResource(propertyPath + "actions.xml");
        System.out.println(propertyPath);
        SAXReader sr = new SAXReader();
        sr.setEntityResolver(new EntityResolver() {

            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                if (systemId.contains("actions.dtd")) return new InputSource(VPResources.class.getResourceAsStream(propertyPath + "actions.dtd")); else return null;
            }
        });
        Document doc = null;
        try {
            doc = sr.read(url.openStream());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();
        List actions = root.elements("Action");
        for (int i = 0; i < actions.size(); i++) {
            Element action = (Element) actions.get(i);
            String name = action.attributeValue("Name");
            boolean noRepeat = Boolean.parseBoolean(action.attributeValue("NoRepeat"));
            boolean noRecord = Boolean.parseBoolean(action.attributeValue("NoRecord"));
            boolean noRemember = Boolean.parseBoolean(action.attributeValue("NoRemember"));
            Element actionCode = action.element("Code");
            String code = actionCode.getText();
            Element actionSelected = action.element("OnSelection");
            String onSelection = null;
            if (actionSelected != null) onSelection = actionSelected.getText();
            addAction(new JythonAction(name, code, onSelection, noRepeat, noRecord, noRemember));
        }
    }
