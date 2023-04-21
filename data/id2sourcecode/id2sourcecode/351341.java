    public static void testSerial(Object obj, String filename) {
        if (writeMode) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
                oos.writeObject(obj);
                oos.close();
            } catch (ObjectStreamException e) {
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bytes);
                oos.writeObject(obj);
                oos.close();
            } catch (ObjectStreamException e) {
            } catch (IOException ioe) {
                fail();
                return;
            }
            byte[] jcl_bytes = bytes.toByteArray();
            int data;
            FileInputStream jdk_file;
            try {
                jdk_file = new FileInputStream(filename);
                for (int i = 0; i < jcl_bytes.length; i++) {
                    data = jdk_file.read();
                    if (data == -1) {
                        fail();
                        return;
                    }
                    if ((byte) data != jcl_bytes[i]) {
                        fail();
                        return;
                    }
                }
                if (jdk_file.read() != -1) {
                    fail();
                    return;
                }
            } catch (IOException e) {
                error();
                return;
            }
            pass();
        }
    }
