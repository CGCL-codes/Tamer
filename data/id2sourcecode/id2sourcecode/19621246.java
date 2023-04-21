    private static Object readObject(URL url) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(url.openStream());
        try {
            try {
                return ois.readObject();
            } catch (ClassNotFoundException e) {
                throw new ApplicationRuntimeException(e);
            }
        } finally {
            ois.close();
        }
    }
