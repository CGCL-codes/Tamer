    private PatchNameMap loadSerial(String name) {
        PatchNameMap pn = null;
        try {
            InputStream is = ClassLoader.getSystemResource("patchnames/" + name + ".pat").openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (is.available() > 0) {
                bos.write(is.read());
            }
            ByteArrayInputStream str = new ByteArrayInputStream(bos.toByteArray());
            try {
                ObjectInputStream ois = new ObjectInputStream(str);
                pn = (PatchNameMap) ois.readObject();
                ois.close();
                str.close();
                return pn;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
