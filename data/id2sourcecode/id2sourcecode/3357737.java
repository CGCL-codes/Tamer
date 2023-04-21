    public void startElement(String namespace, String lName, String qName, Attributes atts) throws SAXException {
        String tag = (lName.equals("")) ? qName : lName;
        if (tag.equals("plugins")) {
            log.info(Messages.getString("PluginHandler.6"));
        }
        if (tag.equals("plugin")) {
            String fileName = atts.getValue("file");
            if (fileName != null) {
                log.info(MessageFormat.format(loading, fileName));
                try {
                    URL url = loadingClass.getResource(fileName);
                    if (url != null) {
                        Reader ir = new InputStreamReader(url.openStream());
                        InputSource is = new InputSource(ir);
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        ModuleHandler nsh = new ModuleHandler(getReader(), this, null, null);
                        sp.parse(is, nsh);
                        ((Module) nsh.getBuildObject()).load();
                        ir.close();
                    } else {
                        Mimosa.displayErrorMessage(null, MessageFormat.format(fileNotFound, fileName), null);
                    }
                } catch (FileNotFoundException e) {
                    Mimosa.displayErrorMessage(null, MessageFormat.format(fileNotFound, fileName), e);
                } catch (FactoryConfigurationError e) {
                    Mimosa.displayErrorMessage(null, Messages.getString("PluginHandler.9"), e);
                } catch (ParserConfigurationException e) {
                    Mimosa.displayErrorMessage(null, Messages.getString("PluginHandler.9"), e);
                } catch (SAXException e) {
                    Mimosa.displayErrorMessage(null, MessageFormat.format(parsingError, fileName), e);
                } catch (IOException e) {
                    Mimosa.displayErrorMessage(null, MessageFormat.format(ioError, fileName), e);
                }
            }
        }
    }
