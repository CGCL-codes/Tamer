        protected Class<?> loadClass(String name, boolean resolve) throws java.lang.ClassNotFoundException {
            if (bcel_jar == null) return super.loadClass(name, resolve);
            if (!name.startsWith("org.apache.bcel") && (!name.startsWith("daikon.chicory.Instrument"))) {
                return super.loadClass(name, resolve);
            }
            Class<?> c = findLoadedClass(name);
            if (c != null) {
                if (resolve) resolveClass(c);
                return c;
            }
            try {
                InputStream is = null;
                if (name.startsWith("daikon.chicory.Instrument")) {
                    String resource_name = classname_to_resource_name(name);
                    URL url = ClassLoader.getSystemResource(resource_name);
                    is = url.openStream();
                } else {
                    String entry_name = classname_to_resource_name(name);
                    JarEntry entry = bcel_jar.getJarEntry(entry_name);
                    assert entry != null : "Can't find " + entry_name;
                    is = bcel_jar.getInputStream(entry);
                }
                int available = is.available();
                byte[] bytes = new byte[available];
                int total = 0;
                while (total < available) {
                    int len = is.read(bytes, total, available - total);
                    total += len;
                }
                assert total == bytes.length : "only read " + total;
                assert is.read() == -1 : "more data left in stream";
                c = defineClass(name, bytes, 0, bytes.length);
                if (resolve) resolveClass(c);
                return c;
            } catch (Exception e) {
                throw new RuntimeException("Unexpected exception loading class " + name, e);
            }
        }
