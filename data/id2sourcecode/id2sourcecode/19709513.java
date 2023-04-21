    public Class<?> findClass(String name) throws ClassNotFoundException {
        log.debug("findClass(" + name + ")");
        Class<?> c = null;
        try {
            URL url = findResource(name);
            if (url == null) {
                throw new ClassNotFoundException(name);
            }
            java.io.InputStream is = url.openStream();
            int count = 0;
            while (is.available() > 0) {
                int elemsRead = is.read(byteBuffer, count, is.available());
                if (elemsRead == -1) {
                    break;
                }
                count += elemsRead;
            }
            c = defineClass(name.substring(0, name.lastIndexOf('.')), byteBuffer, 0, count);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException(name);
        }
        classes.put(c.getName(), c);
        return c;
    }
