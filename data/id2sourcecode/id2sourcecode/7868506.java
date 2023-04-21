    protected void populateFrom(URL url) {
        Properties props = new Properties();
        InputStream is = null;
        try {
            is = url.openStream();
            props.load(is);
            Enumeration<?> names = props.propertyNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = props.getProperty(name);
                String[] values = value.split(";");
                for (int i = 0; i < values.length; i++) {
                    String className = values[i].trim();
                    boolean isExact = className.startsWith("%");
                    if (isExact) className = className.substring(1);
                    try {
                        this.registerWidget(name, Class.forName(className), isExact);
                    } catch (ClassNotFoundException cnfe) {
                    }
                }
            }
        } catch (IOException ioe) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                }
            }
        }
    }
