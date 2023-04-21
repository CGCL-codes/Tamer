                    public Object run() throws Exception {
                        JarURLConnection conn = (JarURLConnection) url.openConnection();
                        conn.setUseCaches(false);
                        return conn.getJarFile();
                    }
