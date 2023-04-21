    static String _fixType(CXManagedObject object, String type) {
        String uti = null;
        String location = null;
        LOG.info("_fixType(): object = " + object);
        try {
            if (object.getClass().equals(Class.forName("org.pachyderm.apollo.data.CXURLObject"))) {
                LOG.info("it's a CXURLObject");
                location = ((CXURLObject) object).url().toString();
                if (location == null) {
                    location = ((CXURLObject) object).identifier();
                }
            } else {
                LOG.info("not a CXURLObject");
                location = (String) object.getValueForAttribute("location");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        LOG.info("fixType(): uti = " + uti);
        LOG.info("fixType(): url string = " + location);
        if (type == null) {
            try {
                URL url = new URL(location);
                LOG.info("url = " + url);
                type = url.openConnection().getContentType();
            } catch (java.io.IOException e) {
            }
        }
        if (type != null) {
            uti = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, type, null);
        }
        if (uti == null) {
            String file = location;
            if (file != null) {
                uti = UTType.preferredIdentifierForTag(UTType.FilenameExtensionTagClass, (NSPathUtilities.pathExtension(file)).toLowerCase(), null);
            }
            if (uti == null) {
                uti = UTType.Item;
            }
        }
        return uti;
    }
