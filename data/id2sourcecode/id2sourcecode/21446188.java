    protected void writeZipFile(Blog blog, List files, OutputStream out) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(out);
        FileRetrieverService frs = FileRetrieverService.getInstance();
        for (Iterator i = files.iterator(); i.hasNext(); ) {
            String path = (String) i.next();
            AbstractFileResource file = frs.getFile(blog, path);
            if (file.isDirectory() || file.isOriginal() || file.getSize() == 0) continue;
            ZipEntry ze = new ZipEntry(path);
            ze.setSize(file.getSize());
            zos.putNextEntry(ze);
            InputStream fis = file.getFileAsInputStream();
            CopyUtils.copy(fis, zos);
            fis.close();
            zos.closeEntry();
        }
        zos.finish();
        zos.flush();
    }
