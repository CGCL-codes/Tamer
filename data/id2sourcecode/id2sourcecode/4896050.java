    protected boolean xmlEquals(BaseObject obj, BaseObject obj2, Object toproot) {
        try {
            if (obj == null || obj2 == null) return false;
            byte[] ba = null;
            if (!xmlToFile) {
                ba = xmlObjectToByteArray("Test", obj);
                xmlByteArrayToObject(obj2, ba, toproot);
            } else {
                StringWriter writer = new StringWriter();
                obj.xmlWriteToWriter(writer, "Test");
                writer.close();
                StringReader reader = new StringReader(writer.toString());
                obj2.xmlReadFromReader(reader, toproot);
            }
            if (obj.junitEquals(obj2)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }
    }
