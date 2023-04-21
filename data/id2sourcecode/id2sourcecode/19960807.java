    private File createFromIS(InputStream inputStream) throws Throwable {
        File f = new File(FILE_DOCUMENT_PATHNAME_FROM_SCANNER + "outFile.jpg");
        OutputStream out = new FileOutputStream(f);
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
        out.close();
        inputStream.close();
        return f;
    }
