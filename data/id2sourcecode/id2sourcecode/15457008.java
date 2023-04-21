    public void setInputStream(InputStream ins) throws IOException {
        int bytes_read = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte data[] = new byte[1024];
        while ((bytes_read = ins.read(data)) > 0) baos.write(data, 0, bytes_read);
        ins.close();
        text_buffer = baos.toString();
        text_area.setText(text_buffer);
    }
