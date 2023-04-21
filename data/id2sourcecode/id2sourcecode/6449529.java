    protected static String getDefaultTemplateFromBundle(String relPath) throws IOException {
        if (relPath == null) throw new IllegalArgumentException("The path of template file can't be null!");
        Bundle bundle = NewZKFilePlugin.getDefault().getBundle();
        InputStream in = FileLocator.openStream(bundle, new Path(relPath), false);
        ByteArrayOutputStream templateData = new ByteArrayOutputStream();
        int offset = 0;
        byte[] buff = new byte[1024 * 256];
        while ((offset = in.read(buff)) != -1) templateData.write(buff, 0, offset);
        return templateData.toString("utf-8");
    }
