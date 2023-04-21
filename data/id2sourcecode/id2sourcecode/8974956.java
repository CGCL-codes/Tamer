    private void saveSnapshot(File file) throws IOException {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(file));
        zip.putNextEntry(new ZipEntry("pc"));
        pc.saveState(zip);
        zip.closeEntry();
        zip.putNextEntry(new ZipEntry("monitor"));
        monitor.saveState(zip);
        zip.closeEntry();
        zip.finish();
        zip.close();
    }
