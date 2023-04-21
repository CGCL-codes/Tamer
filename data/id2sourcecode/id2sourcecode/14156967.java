    protected Template getResourceTemplate(String name) {
        String path = DEFAULT_TEMPLATE_PATH + name + DEFAULT_TEMPLATE_EXT;
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (is == null) throw new GenerationException("Resource not found exception: " + path);
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
            StringWriter sw = new StringWriter();
            int c = -1;
            while ((c = reader.read()) != -1) sw.write(c);
            return templateEngine.createTemplate(sw.toString());
        } catch (Exception e) {
            throw new GenerationException("Could not create template for: " + path, e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
    }
