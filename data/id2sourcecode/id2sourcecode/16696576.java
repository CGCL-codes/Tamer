    public void putNextEntry(String nombre, String datos) throws IOException, ZipException {
        byte[] data = datos.getBytes("UTF-8");
        this.putNextEntry(new ZipEntry(nombre.trim()), data);
    }
