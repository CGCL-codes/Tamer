    public XQueryEnvironment() {
        addResolver("url-resolver", new Resolver() {

            private InputStream resolve(String location) {
                try {
                    URL url = new URL(location);
                    return url.openStream();
                } catch (MalformedURLException e) {
                } catch (IOException e) {
                }
                return null;
            }

            public InputStream resolveSchema(String targetNamespace, String location) {
                return (location == null ? null : resolve(location));
            }

            public InputStream resolveModule(String targetNamespace, String location) {
                return (location == null ? null : resolve(location));
            }
        });
        addResolver("classloader-resolver", new Resolver() {

            private InputStream resolve(String location) {
                InputStream result = this.getClass().getClassLoader().getResourceAsStream(location);
                if (result == null) {
                    result = ClassLoader.getSystemResourceAsStream(location);
                }
                return result;
            }

            public InputStream resolveSchema(String targetNamespace, String location) {
                return (location == null ? null : resolve(location));
            }

            public InputStream resolveModule(String targetNamespace, String location) {
                return (location == null ? null : resolve(location));
            }
        });
        addResolver("current-directory-resolver", new Resolver() {

            private InputStream resolve(String location) {
                try {
                    return new FileInputStream(location);
                } catch (FileNotFoundException e) {
                }
                return null;
            }

            public InputStream resolveSchema(String targetNamespace, String location) {
                return (location == null ? null : resolve(location));
            }

            public InputStream resolveModule(String targetNamespace, String location) {
                return (location == null ? null : resolve(location));
            }
        });
        addSchema(XQueryConstants.XMLSCHEMA_NAMESPACE, "com/doxological/doxquery/builtin.schema");
    }
