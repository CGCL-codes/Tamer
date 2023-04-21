    public final void loadConfigURL(URL url) throws Exception {
        int index;
        String prop, param;
        DataInputStream dis = new DataInputStream(url.openStream());
        String s = dis.readLine();
        if (s.startsWith("TEST")) {
            _mode = TEST_CONFIG;
        } else {
            _mode = DATA_CONFIG;
        }
        s = null;
        while ((s = dis.readLine()) != null) {
            if (s.length() > 2) {
                index = s.indexOf(_command_separator);
                if (index > 0) {
                    prop = s.substring(0, index);
                    param = s.substring(index + 1);
                    addProperty(prop, param);
                }
            }
        }
    }
