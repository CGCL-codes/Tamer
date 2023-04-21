    public String loadConfig(Object source, Integer optionFlags, boolean reDraw) {
        String errors = "";
        Document doc = null;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            if (source instanceof URL) {
                URL url = (URL) source;
                doc = docBuilder.parse(url.openStream());
            } else if (source instanceof File) {
                doc = docBuilder.parse((File) source);
            } else if (source instanceof String) {
                doc = docBuilder.parse(new InputSource(new StringReader((String) source)));
            } else errors += "Can not load from source " + source.getClass().getSimpleName() + ".\n";
        } catch (java.io.IOException ioex) {
            errors += "Could not load file.\n";
        } catch (org.xml.sax.SAXException sex) {
            errors += "Could not parse file.\n";
        } catch (Exception ex) {
            System.out.println(ex);
            errors += "Unknown error.\n";
        }
        if (doc != null) {
            NodeList xmlChildren = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < xmlChildren.getLength(); ++i) {
                Node node = xmlChildren.item(i);
                if (node.getNodeName().equals(XMLNodeName)) errors += this.loadConfig(node.getChildNodes(), optionFlags == null ? FileChooser.OPTION_ALL_MODULES : optionFlags, reDraw);
            }
        }
        return errors;
    }
