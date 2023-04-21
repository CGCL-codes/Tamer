    private File getFileFromDocumento(String uuid) throws Throwable {
        File out = null;
        String user = uuid;
        InputStream in = _documentoManager.getDocumentFile(user, uuid);
        out = new File(FILE_DOCUMENT_TEMP_PATHNAME);
        OutputStream outStream = new FileOutputStream(out);
        byte buf[] = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) outStream.write(buf, 0, len);
        outStream.close();
        in.close();
        assertNotNull(out.length());
        return out;
    }
