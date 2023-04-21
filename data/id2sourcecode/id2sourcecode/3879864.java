    protected String getTextResource(String resourceLocation) throws IOException {
        if (m_resource == null) {
            return "";
        }
        String spec = m_resource.toString();
        int length = ModuleManager.PLUGIN_RESOURCE_LOCATION.length();
        spec = spec.substring(0, spec.length() - length) + resourceLocation;
        URL url = new URL(spec);
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        FileUtil.copyContents(in, out);
        in.close();
        String text = out.toString();
        out.close();
        return text;
    }
