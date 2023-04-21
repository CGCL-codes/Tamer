    private static void loadToBOS(ByteArrayOutputStream bos, String path) {
        FileInputStream fis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(new File(path));
            dis = new DataInputStream(fis);
            for (; ; ) {
                bos.write(dis.readByte());
            }
        } catch (EOFException e) {
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
