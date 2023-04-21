        @Override
        public byte[] findClass(final String name) throws ClassNotFoundException {
            if (log.isDebugEnabled()) {
                log.debug("Finding class " + name);
            }
            try {
                final String classFile = "/" + name.replace('.', '/') + ".class";
                final InputStream input = getClass().getResourceAsStream(classFile);
                if (null == input) {
                    throw new ClassNotFoundException("cannot find class file");
                }
                final ByteArrayOutputStream output = new ByteArrayOutputStream(input.available());
                final byte[] buf = new byte[1024];
                int read;
                while (0 <= (read = input.read(buf))) {
                    output.write(buf, 0, read);
                }
                if (log.isTraceEnabled()) {
                    log.trace("Class found.");
                }
                return output.toByteArray();
            } catch (final IOException e) {
                throw new ClassNotFoundException("cannot read class file", e);
            }
        }
