    public String pContentType() {
        String type = type();
        if (type == null) {
            try {
                URL url = new URL(location());
                type = url.openConnection().getContentType();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        String uti = null;
        if (type != null) {
            uti = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, type, null);
        }
        if (uti == null) {
            String file = location();
            if (file != null) {
                uti = UTType.preferredIdentifierForTag(UTType.FilenameExtensionTagClass, (NSPathUtilities.pathExtension(file)).toLowerCase(), null);
            }
            if (uti == null) {
                uti = UTType.Item;
            }
        }
        return uti;
    }
