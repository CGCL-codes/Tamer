    public static void convert(URL url, PrintWriter writer, String server) {
        try {
            XPathFactory xpf = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
            XPath xpe = xpf.newXPath();
            InputStream is = null;
            try {
                is = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Document doc = readFromStream(is);
            xpe.setNamespaceContext(new NamespaceContext() {

                public String getNamespaceURI(String s) {
                    if (s.equals("tns")) {
                        return "http://services.remote/";
                    } else if (s.equals("xsd")) {
                        return "http://www.w3.org/2001/XMLSchema";
                    } else if (s.equals("soap")) {
                        return "http://schemas.xmlsoap.org/wsdl/soap/";
                    } else if (s.equals("xmlns")) {
                        return "http://schemas.xmlsoap.org/wsdl/";
                    } else if (s.equals("targetNamespace")) {
                        return "http://services.remote/";
                    } else {
                        return null;
                    }
                }

                public String getPrefix(String s) {
                    return null;
                }

                public Iterator getPrefixes(String s) {
                    return null;
                }
            });
            Node schemaLocation = (Node) xpe.compile("/xmlns:definitions/xmlns:types/xsd:schema/xsd:import/@schemaLocation").evaluate(doc, XPathConstants.NODE);
            String sl = schemaLocation.getNodeValue();
            for (int i = 0; i < 3; i++) sl = sl.substring(sl.indexOf('/') + 1);
            schemaLocation.setNodeValue(server + "/" + sl);
            Node location = (Node) xpe.compile("/xmlns:definitions/xmlns:service/xmlns:port/soap:address/@location").evaluate(doc, XPathConstants.NODE);
            String l = location.getNodeValue();
            for (int i = 0; i < 3; i++) l = l.substring(l.indexOf('/') + 1);
            location.setNodeValue(server + "/" + l);
            write(doc, writer);
        } catch (XPathFactoryConfigurationException e) {
            e.printStackTrace();
            System.err.println("Error:" + e);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            System.err.println("Error:" + e);
        }
    }
