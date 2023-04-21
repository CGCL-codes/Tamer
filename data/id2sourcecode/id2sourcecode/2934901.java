    public InputSource resolveEntity(String publicId, String systemId) {
        if (systemId.equalsIgnoreCase("cml.dtd") || systemId.equalsIgnoreCase("CML-1999-05-15.dtd")) {
            try {
                String fname = "org/openscience/jmol/Data/cml.dtd";
                URL url = ClassLoader.getSystemResource(fname);
                InputStream is = url.openStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                return new InputSource(r);
            } catch (Exception exc) {
                System.out.println("Error while trying to read CML DTD: " + exc.toString());
                return null;
            }
        } else {
            return null;
        }
    }
