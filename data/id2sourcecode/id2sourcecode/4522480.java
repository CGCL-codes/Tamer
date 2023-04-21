    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String postData(@FormDataParam("archivo") InputStream archivo, @FormDataParam("tipoArchivo") String tipoArchivo, @FormDataParam("descripcion") String descripcion) {
        String result = "ok";
        logger.debug("tipoArchivo: " + tipoArchivo);
        logger.debug("descripcion: " + descripcion);
        try {
            File f = new File("oooutFile.xml");
            OutputStream out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = archivo.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            archivo.close();
            logger.debug("\nFile is created...................................");
        } catch (IOException e) {
            logger.error(null, e);
        }
        return result;
    }
