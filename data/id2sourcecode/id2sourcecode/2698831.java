    public Tools() {
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
        String[] classes = Classes;
        {
            String test = this.getProperty("alto.io.Tools.classes");
            if (null != test) {
                StringTokenizer strtok = new StringTokenizer(test, ", \r\n\t;:-+|!");
                int count = strtok.countTokens();
                String[] re = new String[count];
                for (int cc = 0; cc < count; cc++) {
                    re[cc] = strtok.nextToken();
                }
                classes = re;
            }
        }
        for (String from : classes) {
            try {
                Class fromClass = Class.forName(from);
                String to = this.getProperty(from);
                if (null != to) {
                    try {
                        Class toClass = Class.forName(to);
                        if (fromClass.isAssignableFrom(toClass)) {
                            try {
                                Method sinit = fromClass.getMethod("SInit", fromClass);
                                Object instance = toClass.newInstance();
                                sinit.invoke(null, instance);
                            } catch (NoSuchMethodException exc) {
                                throw new alto.sys.Error.State(to, exc);
                            } catch (InstantiationException exc) {
                                throw new alto.sys.Error.State(to, exc);
                            } catch (IllegalAccessException exc) {
                                throw new alto.sys.Error.State(to, exc);
                            } catch (InvocationTargetException exc) {
                                throw new alto.sys.Error.State(to, exc);
                            }
                        } else throw new alto.sys.Error.State(toClass + " is not a subclass of " + fromClass);
                    } catch (ClassNotFoundException exc) {
                        throw new alto.sys.Error.State(to, exc);
                    }
                }
            } catch (ClassNotFoundException exc) {
                throw new alto.sys.Error.State(from, exc);
            }
        }
    }
