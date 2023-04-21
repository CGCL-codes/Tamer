    private static Template getTemplate(String inputUrl) throws IOException {
        URL url = FlexibleLocation.resolveLocation(inputUrl);
        URLConnection conn = URLConnector.openConnection(url);
        InputStream in = conn.getInputStream();
        InputStreamReader rdr = new InputStreamReader(in);
        Template template = new Template(inputUrl, rdr, cfg);
        template.setObjectWrapper(BeansWrapper.getDefaultInstance());
        return template;
    }
