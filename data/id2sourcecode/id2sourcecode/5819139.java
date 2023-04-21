    public List<Exporter> loadAvailableExporters() {
        List<Exporter> l = new ArrayList<Exporter>();
        try {
            Enumeration<URL> en = ClassLoader.getSystemClassLoader().getResources("ch/unibe/im2/inkanno/plugins/exporter_implementation.properties");
            while (en.hasMoreElements()) {
                Properties p = new Properties();
                URL url = en.nextElement();
                p.load(url.openStream());
                for (Object objstr : p.keySet()) {
                    String str = (String) objstr;
                    if (str.equals("exporter") || str.startsWith("exporter.")) {
                        if (p.getProperty(str) != null) {
                            l.add(createExporter(p.getProperty(str)));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FactoryException e) {
            e.printStackTrace();
        }
        return l;
    }
