    @SuppressWarnings("unchecked")
    public void scanPlugins() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setEntityResolver(new EntityResolver() {

            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource(new StringReader(""));
            }
        });
        Enumeration<URL> enm = getClass().getClassLoader().getResources("webwheel-plugin.xml");
        while (enm.hasMoreElements()) {
            URL url = enm.nextElement();
            InputStream is = url.openStream();
            Document doc;
            try {
                doc = builder.parse(is);
            } finally {
                is.close();
            }
            String pkg = null;
            Node node = doc.getDocumentElement().getAttributeNode("package");
            if (node != null) {
                pkg = node.getNodeValue();
            }
            NodeList list = doc.getDocumentElement().getElementsByTagName("result-type");
            for (int i = 0; i < list.getLength(); i++) {
                NamedNodeMap as = list.item(i).getAttributes();
                Class type = null;
                node = as.getNamedItem("type");
                if (node == null) {
                    throw new Exception("result-type element need attribute: type at " + url);
                }
                try {
                    type = Class.forName(node.getNodeValue());
                } catch (ClassNotFoundException e) {
                    if (pkg != null) {
                        try {
                            type = Class.forName(pkg + "." + node.getNodeValue());
                        } catch (ClassNotFoundException e2) {
                        }
                    }
                    if (type == null) throw new Exception("can not find class: " + node.getNodeValue() + " at " + url);
                }
                node = as.getNamedItem("interpreter");
                if (node == null) {
                    throw new Exception("result-type element need attribute: interpreter at " + url);
                }
                Class<? extends ResultInterpreter> interpreter = null;
                try {
                    interpreter = (Class<? extends ResultInterpreter>) Class.forName(node.getNodeValue());
                } catch (ClassNotFoundException e) {
                    if (pkg != null) {
                        try {
                            interpreter = (Class<? extends ResultInterpreter>) Class.forName(pkg + "." + node.getNodeValue());
                        } catch (ClassNotFoundException e2) {
                        }
                    }
                    if (interpreter == null) throw new Exception("can not find class: " + node.getNodeValue() + " at " + url);
                }
                if (!ResultInterpreter.class.isAssignableFrom(interpreter)) {
                    throw new Exception(interpreter + " is not result interpreter at " + url);
                }
                registerResultType(type, getInstanceOf(interpreter));
            }
            list = doc.getDocumentElement().getElementsByTagName("component");
            for (int i = 0; i < list.getLength(); i++) {
                NamedNodeMap as = list.item(i).getAttributes();
                Class type = null;
                node = as.getNamedItem("class");
                if (node == null) {
                    throw new Exception("component element need attribute: class at " + url);
                }
                try {
                    type = Class.forName(node.getNodeValue());
                } catch (ClassNotFoundException e) {
                    if (pkg != null) {
                        try {
                            type = Class.forName(pkg + "." + node.getNodeValue());
                        } catch (ClassNotFoundException e2) {
                        }
                    }
                    if (type == null) throw new Exception("can not find class: " + node.getNodeValue() + " at " + url);
                }
                String charset = "utf-8";
                node = as.getNamedItem("charset");
                if (node != null) {
                    charset = node.getNodeValue();
                }
                node = as.getNamedItem("template");
                if (node == null) {
                    throw new Exception("component element need attribute: template at " + url);
                }
                getInstanceOf(TemplateResultInterpreter.class).bindTemplate(type, TemplateResultInterpreter.ClassPath + node.getNodeValue(), charset);
            }
        }
    }
