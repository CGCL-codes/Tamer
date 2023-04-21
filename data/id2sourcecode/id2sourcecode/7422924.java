    private static void examineBadFile(File file, boolean descend) {
        Log.warn("BAD DROPPED FILE:" + "\n\t      file: " + file + "\n\t      name: " + Util.tags(file.getName()) + "\n\t    exists: false" + "\n\t   canRead: " + file.canRead());
        URI uri = null;
        String nameUTF = null;
        String nameMac = null;
        File fileUTF = null;
        File fileMac = null;
        File uriUTF = null;
        File uriMac = null;
        try {
            uri = file.toURI();
            nameUTF = java.net.URLEncoder.encode(file.getName(), "UTF-8");
            nameMac = java.net.URLEncoder.encode(file.getName(), "MacRoman");
            fileUTF = new File(file.getParent(), nameUTF);
            fileMac = new File(file.getParent(), nameMac);
            URI utf, mac;
            utf = new URI("file://" + file.getParent() + File.separator + nameUTF);
            mac = new URI("file://" + file.getParent() + File.separator + nameMac);
            Log.debug("URIUTF " + utf);
            Log.debug("URIMac " + mac);
            uriUTF = new File(utf);
            uriMac = new File(mac);
            URL url = new URL(utf.toString());
            Log.debug("URL: " + url);
            URLConnection c = url.openConnection();
            Log.debug("URL-CONTENT: " + Util.tags(c.getContent()));
        } catch (Throwable t) {
            Log.error("meta-debug", t);
        }
        Log.warn("BAD DROPPED FILE ANALYSIS:" + "\n\t     toURI: " + uri + "\n\t    asUTF8: " + Util.tags(nameUTF) + "\n\tasMacRoman: " + Util.tags(nameMac) + "\n\t   fileUTF: " + fileUTF + "\n\t existsUTF: " + fileUTF.exists() + "\n\t   fileMac: " + fileMac + "\n\t existsMac: " + fileMac.exists() + "\n\t    uriUTF: " + uriUTF + "\n\t existsUTF: " + uriUTF.exists() + "\n\t    uriMac: " + uriMac + "\n\t existsMac: " + uriMac.exists() + "\n\t(probably contains unicode character(s) unhandled by java: this is a java bug)");
    }
