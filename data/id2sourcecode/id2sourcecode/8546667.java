        public StringTemplateGroup resolveSTG(ProtoModule module) {
            String resource = module.getOutput();
            try {
                File file = new File(resource);
                if (file.exists()) return new StringTemplateGroup(new BufferedReader(new FileReader(file)));
                URL url = DefaultProtoLoader.getResource(resource, PluginProtoCompiler.class);
                if (url != null) {
                    return new StringTemplateGroup(new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")));
                }
                if (resource.startsWith("http://")) {
                    return new StringTemplateGroup(new BufferedReader(new InputStreamReader(new URL(resource).openStream(), "UTF-8")));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            throw new IllegalStateException("Could not find " + resource);
        }
