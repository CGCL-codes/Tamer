    public void launch(JadFile jadFileParam, int midletNr) {
        if (jadFileParam != null) {
            jadFile = jadFileParam;
        } else {
            jadFile = new JadFile();
            String jadUrl = getProperty("jad");
            if (jadUrl != null) {
                try {
                    jadFile.load(jadUrl);
                } catch (Exception e) {
                    System.err.println("JAD access error: " + e + "; trying midlet/jam property");
                }
            }
        }
        JadFile manifest = new JadFile();
        try {
            InputStream is = this.getClass().getResourceAsStream("/me4se.properties");
            if (is != null) {
                manifest.load(is);
            } else {
                Enumeration e = this.getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
                while (e.hasMoreElements()) {
                    URL url = (URL) e.nextElement();
                    manifest.load(url.openStream());
                    if (manifest.getMIDletCount() != 0) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error while reading/searching MANIFEST.MF");
            e.printStackTrace();
        }
        if (manifest.getMIDletCount() > 0) {
            for (Enumeration e = manifest.keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                if (key != null) {
                    String value = manifest.getValue(key);
                    if (jadFile.getValue(key) == null) {
                        jadFile.setValue(key, value);
                    }
                    properties.setProperty(key, value);
                    if (key.startsWith("microedition.")) {
                        setSystemProperty(key, value);
                    }
                }
            }
        }
        if (jadFile.getValue("MIDlet-1") == null) {
            String midlet = getProperty("MIDlet");
            if (midlet != null) {
                jadFile.setValue("MIDlet-1", midlet + ",," + midlet);
            } else {
                throw new RuntimeException("JAD File does not contain 'MIDLet' or 'MIDLet-1' property");
            }
        }
        try {
            if (jadFile.getMIDletCount() == 1 || midletNr > 0) {
                activeClass = Class.forName(jadFile.getMIDlet(Math.max(midletNr, 1)).getClassName());
                active = ((MIDlet) activeClass.newInstance());
            } else active = new MIDletChooser();
            Displayable d = Display.getDisplay(active).getCurrent();
            if (d != null) {
                Display.getDisplay(active).setCurrent(d);
            }
            active.startApp();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
