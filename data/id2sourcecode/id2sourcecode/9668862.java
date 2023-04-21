    private Map createArrowMap() {
        Map map = new HashMap();
        Properties props = new Properties();
        URL url = getClass().getResource("arrow.properties");
        InputStream strm = null;
        try {
            strm = url.openStream();
            props.load(strm);
            for (Enumeration e = props.keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                ImageIcon icon = new ImageIcon(getClass().getResource(props.getProperty(key)));
                map.put(key, icon);
            }
        } catch (Exception e) {
            LogFactory.getLog(getClass()).warn("failed loading plug-in settings", e);
        } finally {
            if (strm != null) try {
                strm.close();
            } catch (IOException e) {
            }
        }
        return Collections.unmodifiableMap(map);
    }
