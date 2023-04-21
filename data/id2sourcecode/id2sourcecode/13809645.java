    private void writeEntry(TarOutputStream tos, TarEntry entry, InputStream data) throws IOException {
        tos.putNextEntry(entry);
        byte[] newBytes = InputStreamUtil.createReadBuffer();
        for (int size = data.read(newBytes); size != -1; size = data.read(newBytes)) tos.write(newBytes, 0, size);
        data.close();
        tos.closeEntry();
    }
