    private PatchNameMap loadTxt(String name) {
        try {
            InputStream is = ClassLoader.getSystemResource("patchnames/" + name + ".txt").openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (is.available() > 0) {
                bos.write(is.read());
            }
            ByteArrayInputStream str = new ByteArrayInputStream(bos.toByteArray());
            PatchNameMap sw = new PatchNameMap(str);
            return sw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
