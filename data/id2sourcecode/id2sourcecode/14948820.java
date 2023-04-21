    public ModelOficioUploader(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws SQLException, Exception {
        log = Logger.getLogger(this.getClass());
        FileUpload fu = new FileUpload(new DefaultFileItemFactory());
        fu.setSizeMax(MAX_FILE_SIZE);
        ModeloOficioFacade mod = ModeloOficioFacadeUtil.getHome().create();
        java.util.List fileItems = fu.parseRequest(request);
        java.util.Iterator i = fileItems.iterator();
        boolean borrar = false;
        String nombre = null;
        FileItem datos = null;
        while (i.hasNext()) {
            FileItem item = (FileItem) i.next();
            if (item.getFieldName().equals("borra_model") && item.getString().equals("s")) {
                borrar = true;
            } else if (item.getFieldName().equals("nombre")) {
                nombre = item.getString();
            } else if (item.getSize() > 0) {
                datos = item;
            }
        }
        if (borrar) {
            try {
                mod.eliminar(nombre);
                borrado = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (datos.getFieldName().equals("fitxer")) {
            try {
                InputStream uploadedStream = datos.getInputStream();
                java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                int iRead = 0;
                byte[] baChunk = new byte[4096];
                while ((iRead = uploadedStream.read(baChunk)) > 0) baos.write(baChunk, 0, iRead);
                byte[] baResult = baos.toByteArray();
                uploadedStream.close();
                mod.grabar(nombre, datos.getContentType(), baResult);
                grabado = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
