        private Class def(String name) throws ClassNotFoundException {
            Class result = (Class) classes.get(name);
            if (result != null) {
                return result;
            }
            try {
                java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream(name.replace('.', '\\') + ".class");
                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
                while (is.available() > 0) {
                    out.write(is.read());
                }
                byte data[] = out.toByteArray();
                result = super.defineClass(name, data, 0, data.length);
                classes.put(name, result);
                return result;
            } catch (java.io.IOException ioe) {
                throw new ClassNotFoundException(name + " caused by " + ioe.getMessage());
            }
        }
