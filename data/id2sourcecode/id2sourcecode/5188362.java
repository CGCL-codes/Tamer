        public Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                logWriter.println("-->> CustomClassLoader: Find class: " + name);
                String res = name.replace('.', '/') + ".class";
                URL url = getResource(res);
                logWriter.println("-->> CustomClassLoader: Found class file: " + res);
                InputStream is = url.openStream();
                int size = 1024;
                byte bytes[] = new byte[size];
                int len = loadClassData(is, bytes, size);
                logWriter.println("-->> CustomClassLoader: Loaded class bytes: " + len);
                Class cls = defineClass(name, bytes, 0, len);
                logWriter.println("-->> CustomClassLoader: Defined class: " + cls);
                return cls;
            } catch (Exception e) {
                throw new ClassNotFoundException("Cannot load class: " + name, e);
            }
        }
