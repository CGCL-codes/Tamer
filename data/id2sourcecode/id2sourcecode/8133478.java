        @SuppressWarnings("unchecked")
        protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
            Class c = super.findLoadedClass(name);
            if (c != null) {
                return c;
            }
            if (name.startsWith("java.")) {
                return super.loadClass(name, resolve);
            } else if (name.startsWith("javax.")) {
                return super.loadClass(name, resolve);
            } else if (name.startsWith("org.datanucleus.jpa.annotations") || name.startsWith("org.datanucleus.api.jpa.annotations")) {
                return super.loadClass(name, resolve);
            }
            String resource = StringUtils.replaceAll(name, ".", "/") + ".class";
            try {
                URL url = super.getResource(resource);
                if (url == null) {
                    throw new ClassNotFoundException(name);
                }
                InputStream is = url.openStream();
                try {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    byte[] b = new byte[2048];
                    int count;
                    while ((count = is.read(b, 0, 2048)) != -1) {
                        os.write(b, 0, count);
                    }
                    byte[] bytes = os.toByteArray();
                    return defineClass(name, bytes, 0, bytes.length);
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            } catch (SecurityException e) {
                return super.loadClass(name, resolve);
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }
        }
