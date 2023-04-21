    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        context.popElement();
        Helper.getLogger().debug("Processing Tag </" + qName + ">");
        try {
            if (qName.equalsIgnoreCase(IConstants.tPARAM)) {
                if (context.isInContext(IConstants.tFUNCTION)) {
                    String extern = actAttributes.getValue(IConstants.aEXTERN);
                    if (extern != null) {
                        URL url = manager.getResource(extern);
                        actContent = Helper.readStream(url.openStream());
                    }
                    builder.addFunctionParameter(actContent);
                }
            }
            Iterator iter = extensions.iterator();
            while (iter.hasNext()) {
                IExtension extension = (IExtension) iter.next();
                String[] extraTags = extension.getExtraTags();
                for (int i = 0; i < extraTags.length; i++) {
                    if (qName.equalsIgnoreCase(extraTags[i])) {
                        extension.handleExtraTag(i, actContent);
                    }
                }
            }
        } catch (IOException e) {
            throw new SAXParseException(null, locator, e);
        } catch (BuilderException e) {
            throw new SAXParseException(null, locator, e);
        }
    }
