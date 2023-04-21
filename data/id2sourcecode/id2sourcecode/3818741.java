    public void export(IExportSet exportSet, OutputStream os) throws IOException {
        ZipOutputStream out = new ZipOutputStream(os);
        for (IExportUnit eu : exportSet.getExportUnits()) {
            ZipEntry entry = new ZipEntry(eu.getRelativePath(this));
            out.putNextEntry(entry);
            eu.export(out, this);
            out.closeEntry();
        }
        out.close();
    }
