    protected void init() throws JAXBException {
        if (packageNames == null || packageNames.length() == 0) {
            packageNames = JAXBContextImpl.class.getName();
            packageNames = packageNames.substring(0, packageNames.lastIndexOf('.'));
        }
        boolean first = true;
        for (StringTokenizer st = new StringTokenizer(packageNames, ":"); st.hasMoreTokens(); ) {
            String packageName = st.nextToken();
            String configFileName = ((packageName.length() > 0) ? (packageName.replace('.', '/') + '/') : "") + "Configuration.xml";
            URL url = getClassLoader().getResource(configFileName);
            if (url != null) {
                InputStream istream = null;
                try {
                    Configuration c = new Configuration(this);
                    Configurator configurator = new Configurator();
                    configurator.setNamespace(CONFIGURATION_URI);
                    configurator.setRootObject(c);
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    xr.setContentHandler(configurator);
                    istream = url.openStream();
                    InputSource isource = new InputSource(istream);
                    isource.setSystemId(url.toString());
                    xr.parse(isource);
                    istream.close();
                    istream = null;
                    if (first) {
                        first = false;
                        setJMMarshallerClass(c.getJMMarshallerClass());
                        setJMUnmarshallerClass(c.getJMUnmarshallerClass());
                        setJMValidatorClass(c.getJMValidatorClass());
                    }
                } catch (IOException e) {
                    throw new JAXBException("Failed to load config file " + url, e);
                } catch (SAXParseException e) {
                    Exception f = e.getException() == null ? e : e.getException();
                    throw new JAXBException("Failed to parse config file " + url + " at line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + f.getMessage(), f);
                } catch (SAXException e) {
                    Exception f = e.getException() == null ? e : e.getException();
                    String msg = "Failed to parse config file " + url + ": " + f.getMessage();
                    throw new JAXBException(msg, f);
                } catch (ParserConfigurationException e) {
                    throw new JAXBException("Failed to create a SAX Parser: " + e.getMessage(), e);
                } finally {
                    if (istream != null) {
                        try {
                            istream.close();
                        } catch (Throwable ignore) {
                        }
                    }
                }
            }
        }
        if (first) {
            throw new JAXBException("Unable to locate configuration file Configuration.xml in " + packageNames);
        }
    }
