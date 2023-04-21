    public static Object readObject(Object obj, String fileName) throws Exception {
        InputStream input = null;
        ObjectInputStream oinput = null;
        URL url = SerializationTester.class.getClassLoader().getResource(fileName);
        if (null == url) {
            writeObject(obj, new File(fileName).getName());
            throw new Error("Serialization file does not exist, created in the current dir.");
        }
        input = url.openStream();
        try {
            oinput = new ObjectInputStream(input);
            Object newObj = oinput.readObject();
            return newObj;
        } finally {
            try {
                if (null != oinput) {
                    oinput.close();
                }
            } catch (Exception e) {
            }
            try {
                if (null != input) {
                    input.close();
                }
            } catch (Exception e) {
            }
        }
    }
