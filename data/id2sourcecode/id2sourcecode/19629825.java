        private Tools() {
            super();
            URL url = this.getClass().getResource(Resource);
            if (null != url) {
                try {
                    InputStream in = url.openStream();
                    try {
                        super.load(in);
                    } finally {
                        in.close();
                    }
                } catch (IOException exc) {
                    throw new alto.sys.Error.State(Resource, exc);
                }
            }
            int count = this.size(), cc = 0;
            Method[] list = new Method[count];
            java.util.Enumeration keys = this.keys();
            while (keys.hasMoreElements()) {
                String authtname = (String) keys.nextElement();
                Authentication type = Authentication.Lookup(authtname);
                if (null != type) {
                    String classname = (String) this.get(authtname);
                    try {
                        Class clas = Class.forName(classname);
                        Method method = (Method) clas.newInstance();
                        method.setKind(type);
                        this.put(authtname, method);
                        list[cc++] = method;
                    } catch (ClassNotFoundException exc) {
                        throw new alto.sys.Error.State(classname, exc);
                    } catch (InstantiationException exc) {
                        throw new alto.sys.Error.State(classname, exc);
                    } catch (IllegalAccessException exc) {
                        throw new alto.sys.Error.State(classname, exc);
                    }
                } else throw new alto.sys.Error.State("Unrecognized authentication type '" + authtname + "'.");
            }
            this.list = list;
        }
