    public static Factory staffFactory(URL url, Factory parent) throws ExternalConfigException, IOException, NoSuchMethodException, ParseException, ClassNotFoundException {
        Object blueprint;
        InputStream is = url.openStream();
        try {
            blueprint = ObjectParser.parse(is);
            if (!(blueprint instanceof DTObject)) {
                throw new ExternalConfigException("Root value of the HRDT" + " configuration file must be an object");
            }
        } finally {
            is.close();
        }
        return staffFactory((DTObject) blueprint, parent, url.toString());
    }
