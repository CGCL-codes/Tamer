    private void checkForExtraLibraries() throws Exception {
        StringTokenizer tokenizer = new StringTokenizer(extraResource, ",;");
        String plugin_lib_dir = XProjectConstants.PLUGINS_DIR + name + "_lib/";
        String extra_resource_urls = "";
        String temp_rsrc;
        InputStream is;
        OutputStream os;
        File file;
        URL url;
        int b;
        while (tokenizer.hasMoreTokens()) {
            temp_rsrc = tokenizer.nextToken();
            file = new File(plugin_lib_dir + temp_rsrc);
            if (!file.exists()) {
                url = pluginClassLoader.getResource(temp_rsrc);
                is = url.openStream();
                file.getParentFile().mkdirs();
                os = new FileOutputStream(file);
                while ((b = is.read()) != -1) os.write(b);
                is.close();
                os.close();
            }
            if (!extra_resource_urls.equals("")) extra_resource_urls += ";";
            extra_resource_urls += plugin_lib_dir + temp_rsrc;
        }
        pluginClassLoader = XRepository.getResourceManager().createBuiltInClassLoader(extra_resource_urls, pluginClassLoader);
    }
