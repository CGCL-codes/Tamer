    public void initSource(String source, Step step) throws MigrationException {
        this.source = source;
        this.step = step;
        columnMapping.clear();
        try {
            if (sp == null) {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                sp = spf.newSAXParser();
                XMLReader reader = sp.getXMLReader();
                reader.setContentHandler(this);
            } else {
                sp.reset();
            }
            if (jar == null) {
                URL url = new URL(this.url);
                URLConnection con = url.openConnection();
                if (con instanceof JarURLConnection) {
                    jar = ((JarURLConnection) con).getJarFile();
                }
            }
            firstRow = new HashMap<String, String>();
            record = null;
            path = "";
        } catch (MalformedURLException me) {
        } catch (Exception e) {
            throw new MigrationException(e);
        }
    }
