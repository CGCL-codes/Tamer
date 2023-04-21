    public URL convert(URL url) {
        if (url != null && url.getProtocol().startsWith("vfs")) {
            try {
                URLConnection connection = url.openConnection();
                Object virtualFile = invokerGetter(connection, "getContent");
                Object zipEntryHandler = invokerGetter(virtualFile, "getHandler");
                Object realUrl = invokerGetter(zipEntryHandler, "getRealURL");
                return (URL) realUrl;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return url;
    }
