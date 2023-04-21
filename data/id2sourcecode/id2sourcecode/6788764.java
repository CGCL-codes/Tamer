    public String getMainClassName() {
        String name = null;
        try {
            URL url = new URL("jar", "", mUrl + "!/");
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            Attributes attr = conn.getMainAttributes();
            if (attr != null) {
                name = attr.getValue(Attributes.Name.MAIN_CLASS);
            }
        } catch (Exception ex) {
        }
        return name;
    }
