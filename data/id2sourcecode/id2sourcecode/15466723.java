    public void importXmlFile(File importFile, final String importHandler, final PrintWriter reporter, final Properties properties) {
        XMLReader reader = null;
        try {
            reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new DefaultHandler() {

                boolean importNodeFound = false;

                String currentTag = null;

                ImportHandler currentHandler = null;

                StringBuilder tagContent = new StringBuilder();

                @Override
                public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
                    if (!importNodeFound) {
                        if ("import".equals(localName)) {
                            importNodeFound = true;
                        }
                        return;
                    }
                    if (currentTag == null) {
                        ImportHandlerConfig ihc = getImportHandlerConfigByRoot(localName);
                        if (ihc == null || (importHandler != null && !ihc.getId().equals(importHandler))) {
                            return;
                        }
                        currentTag = localName;
                        currentHandler = ihc.getHandler();
                        currentHandler.startRootElement(uri, localName, name, attributes, reporter, properties);
                        return;
                    }
                    if (currentHandler != null) {
                        tagContent = new StringBuilder();
                        currentHandler.startElement(uri, localName, name, attributes, reporter, properties);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String name) throws SAXException {
                    if (localName.equals(currentTag)) {
                        currentHandler.endRootElement(uri, localName, name, reporter, properties);
                        currentTag = null;
                        currentHandler = null;
                        return;
                    }
                    if (currentHandler != null) {
                        boolean allwhite = true;
                        int j = 0;
                        while ((j < tagContent.length()) && (allwhite)) {
                            allwhite = Character.isWhitespace(tagContent.charAt(j));
                            ++j;
                        }
                        if (!allwhite) {
                            currentHandler.elementContent(uri, localName, name, StringTools.trim(tagContent.toString()), reporter, properties);
                        }
                        tagContent = new StringBuilder();
                        currentHandler.endElement(uri, localName, name, reporter, properties);
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    tagContent.append(ch, start, length);
                }
            });
            reader.setErrorHandler(new DefaultHandler() {

                @Override
                public void error(SAXParseException e) throws SAXException {
                    reporter.write("Error: " + e.getMessage());
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                    reporter.write("Error: " + e.getMessage());
                }

                @Override
                public void warning(SAXParseException e) throws SAXException {
                    reporter.write("Error: " + e.getMessage());
                }
            });
        } catch (SAXException x) {
            reporter.write("Error: Unable to create XML reader");
            return;
        }
        try {
            reader.parse(new InputSource(new FileReader(importFile)));
        } catch (FileNotFoundException x) {
            reporter.write("Error: Import file '" + importFile.getAbsolutePath() + "' not found: " + x.getMessage());
        } catch (IOException x) {
            reporter.write("Error: " + x.getMessage());
        } catch (SAXException x) {
            reporter.write("Error: " + x.getMessage());
        }
    }
