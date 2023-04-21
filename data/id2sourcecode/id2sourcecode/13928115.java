            public List<Object> run() {
                List<Object> providers = new ArrayList<Object>();
                String className = null;
                byte[] bytes;
                ClassLoader cl = ClassLoader.getSystemClassLoader();
                Enumeration<URL> urls = null;
                try {
                    urls = cl.getResources(name);
                } catch (IOException e) {
                    return providers;
                }
                for (; urls.hasMoreElements(); ) {
                    try {
                        InputStream in = urls.nextElement().openStream();
                        bytes = InputStreamHelper.readFullyAndClose(in);
                    } catch (IOException e) {
                        continue;
                    }
                    String[] astr = new String(bytes).split("\r\n");
                    for (String str : astr) {
                        className = str.trim();
                        if (!className.startsWith("#")) {
                            try {
                                providers.add(Class.forName(className.trim(), true, cl).newInstance());
                            } catch (IllegalAccessException e) {
                            } catch (InstantiationException e) {
                            } catch (ClassNotFoundException e) {
                            }
                        }
                    }
                }
                return providers;
            }
