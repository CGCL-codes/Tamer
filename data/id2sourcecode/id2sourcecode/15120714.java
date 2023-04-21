    public RequestWrapper loadMultipartParameters(HttpServletRequest request) {
        final int _20MB = 20 * 1024 * 1024;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(_20MB);
        RequestWrapper params = new RequestWrapper(10);
        try {
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString();
                    params.add(name, value);
                } else {
                    String PARTIAL_PATH_SOLLECITO_FATTURA = "tmp";
                    String fotoPath = "C:";
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();
                    if (sizeInBytes > 0 && fieldName != null) {
                        File uploadedFile = new File(fotoPath + fileName);
                        try {
                            InputStream istream = item.getInputStream();
                            if (uploadedFile.exists()) {
                                uploadedFile.renameTo(new File(fotoPath + "new" + fileName));
                            } else {
                                uploadedFile.createNewFile();
                            }
                            OutputStream out = new FileOutputStream(uploadedFile);
                            int read = 0;
                            byte[] bytes = new byte[1024];
                            while ((read = istream.read(bytes)) != -1) {
                                out.write(bytes, 0, read);
                            }
                            istream.close();
                            out.flush();
                            out.close();
                            item.write(uploadedFile);
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        } finally {
                        }
                    }
                }
            }
        } catch (FileUploadException fuexc) {
            fuexc.printStackTrace();
        }
        return params;
    }
