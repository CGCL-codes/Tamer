    private byte[] loadClassData(String name) throws ClassNotFoundException {
        if (debug) {
            System.err.println("loadClassData: " + baseURL);
        }
        if (name.endsWith(".class")) {
            name = name.substring(0, name.length() - 6);
        }
        name = name.replace('.', '/');
        name += ".class";
        InputStream is = null;
        try {
            URL url = new URL(baseURL, name);
            is = url.openStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            return os.toByteArray();
        } catch (MalformedURLException e) {
            throw new ClassNotFoundException(e.toString());
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
