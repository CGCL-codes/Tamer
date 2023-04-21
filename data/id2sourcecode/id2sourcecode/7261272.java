    private void downloadFile() throws IOException {
        int default_buffer_size = 2048;
        BufferedInputStream is = new BufferedInputStream(source.openStream(), default_buffer_size);
        FileOutputStream fo = new FileOutputStream(target);
        BufferedOutputStream bos = new BufferedOutputStream(fo, default_buffer_size);
        int read = 0;
        while ((read = is.read()) != -1 && !abort) {
            read_position++;
            bos.write(read);
        }
        is.close();
        bos.flush();
        bos.close();
    }
