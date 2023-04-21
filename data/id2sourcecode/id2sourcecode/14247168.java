    public static String convertStreamToString(final InputStream is, final String name) throws IOException {
        String f;
        String nFolder = APP_FOLDER;
        if ((new File(XMLHandler.folder)).mkdirs()) {
            nFolder = XMLHandler.folder + "/";
        }
        f = (nFolder + name);
        File file = new File(f);
        OutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) out.write(buf, 0, len);
        out.close();
        is.close();
        return "stored in " + file.getPath();
    }
