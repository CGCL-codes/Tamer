    public AttachmentDataSource(String name, String contenttype, InputStream inputstream) throws IOException {
        this.name = name;
        this.contenttype = contenttype;
        BufferedInputStream lector = new BufferedInputStream(inputstream, 25000);
        ByteArrayOutputStream aux = new ByteArrayOutputStream();
        BufferedOutputStream escritor = new BufferedOutputStream(aux);
        byte[] buffer = new byte[10000];
        int leidos = 0;
        while ((leidos = inputstream.read(buffer, 0, 10000)) > 0) escritor.write(buffer, 0, leidos);
        lector.close();
        escritor.close();
        datos = aux.toByteArray();
    }
