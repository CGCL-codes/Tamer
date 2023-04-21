    private static byte[] loadFile(String file) {
        int next;
        java.util.Vector bytes = new java.util.Vector();
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            URL url = loader.getResource(file);
            InputStream stream = new BufferedInputStream(url.openStream());
            while ((next = (stream.read())) != -1) bytes.add(new Byte((byte) next));
            stream.close();
            byte[] result = new byte[bytes.size()];
            for (int i = 0; i < result.length; i++) result[i] = ((Byte) bytes.get(i)).byteValue();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
