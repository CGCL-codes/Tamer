        public Class loadClass(String name) throws ClassNotFoundException {
            try {
                return super.findSystemClass(name);
            } catch (ClassNotFoundException x) {
                try {
                    String url = path + name.replace('.', '/') + ".class";
                    InputStream is = new URL(url).openConnection().getInputStream();
                    byte bytes[] = new byte[is.available()];
                    is.read(bytes);
                    is.close();
                    Class result = defineClass(name, bytes, 0, bytes.length);
                    resolveClass(result);
                    return result;
                } catch (IOException y) {
                    return null;
                }
            }
        }
