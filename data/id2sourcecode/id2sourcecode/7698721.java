    public String pContentType() {
        System.out.println("AssetDBRecord.pContentType(): executing\n");
        String type = type();
        System.out.println("AssetDBRecord.pContentType(): type == " + type + "\n");
        String uti = null;
        if (type == null) {
            try {
                String location = location();
                System.out.println("AssetDBRecord.pContentType(): location = " + location + "\n");
                URL url = new URL(location());
                type = url.openConnection().getContentType();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        if (type != null) {
            uti = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, type, null);
            System.out.println("AssetDBRecord.pContentType(): uti = " + uti + "\n");
        }
        if (uti == null) {
            String file = location();
            if (file != null) {
                uti = UTType.preferredIdentifierForTag(UTType.FilenameExtensionTagClass, (NSPathUtilities.pathExtension(file)).toLowerCase(), null);
                System.out.println("AssetDBRecord.pContentType(): uti = " + uti + "\n");
            }
            if (uti == null) {
                uti = UTType.Item;
            }
        }
        return uti;
    }
