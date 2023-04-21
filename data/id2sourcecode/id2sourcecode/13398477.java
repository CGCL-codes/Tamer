    private boolean exists(Vector schema_import, String ns_uri) {
        Object[] schemas = new Object[schema_import.size()];
        schema_import.copyInto(schemas);
        String[] uriList = new String[schemas.length];
        for (int i = 0; i < uriList.length; i++) {
            uriList[i] = (String) schema_import.elementAt(i);
            File f = new File(uriList[i]);
            if (f.exists()) return true;
            try {
                URL url = new URL(uriList[i]);
                URLConnection con = url.openConnection();
                con.getInputStream();
                return true;
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
        }
        if (getCurrentContext().getLocationOfSchema(ns_uri) != null) {
            schema_import.addElement(getCurrentContext().getLocationOfSchema(ns_uri));
            return true;
        }
        return false;
    }
