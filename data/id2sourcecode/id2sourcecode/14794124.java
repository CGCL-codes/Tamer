    public void registerHelpers(ComponentHelper componentHelpers) {
        componentHelpers.addComponentType(new CheckBoxHelper(), false);
        componentHelpers.addComponentType(new ComboBoxHelper(), false);
        componentHelpers.addComponentType(new LabelHelper(), false);
        componentHelpers.addComponentType(new ListHelper(false), false);
        componentHelpers.addComponentType(new MenuHelper(), false);
        componentHelpers.addComponentType(new MenuItemHelper(), false);
        componentHelpers.addComponentType(new PanelHelper(), false);
        componentHelpers.addComponentType(new ScrollPaneHelper(), false);
        componentHelpers.addComponentType(new RadioButtonHelper(), false);
        componentHelpers.addComponentType(new TableHelper(), false);
        componentHelpers.addComponentType(new TextAreaHelper(), false);
        componentHelpers.addComponentType(new IncludeReferenceHelper(), false);
        componentHelpers.addComponentType(new RepeatReferenceHelper(), false);
        try {
            URL url = getClass().getClassLoader().getResource("org/formaria/editor/project/pages/components/properties.xml");
            Reader r = new BufferedReader(new InputStreamReader(url.openStream()));
            XmlElement regRoot = XmlSource.read(r);
            Vector registrationNodes = regRoot.getChildren();
            int numElements = registrationNodes.size();
            for (int i = 0; i < numElements; i++) {
                XmlElement regElement = (XmlElement) registrationNodes.elementAt(i);
                GenericPropertyHelper ph = new GenericPropertyHelper();
                ph.setClassName(regElement.getAttribute("class"));
                ph.setComponentType(regElement.getAttribute("name"));
                String s = regElement.getAttribute("allowsChildren");
                if ((s != null) && "true".equals(s)) ph.setAllowsChildren(true);
                s = regElement.getAttribute("usesContentFile");
                if ((s != null) && "true".equals(s)) ph.setUsesContentFile(true);
                s = regElement.getAttribute("restrictsSize");
                if ((s != null) && "true".equals(s)) ph.setRestrictsSize(true);
                Vector propNodes = regElement.getChildren();
                int numProps = propNodes.size();
                for (int j = 0; j < numProps; j++) {
                    XmlElement propElement = (XmlElement) propNodes.elementAt(j);
                    String tag = propElement.getName();
                    if (tag.equals("extension")) {
                        String[] values = propElement.getAttribute("values").split(";");
                        ph.setFileExtensions(propElement.getAttribute("desc"), propElement.getAttribute("default"), values);
                    } else {
                        String propName = propElement.getAttribute("name");
                        ph.addProperty(propName);
                        String defValue = propElement.getAttribute("default");
                        if ((defValue != null) && (defValue.length() > 0)) ph.addDefaultValues(propName, defValue);
                    }
                }
                componentHelpers.addComponentType(ph);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
