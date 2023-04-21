    private void setMessage(InputStream istr) throws IOException {
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int read = istr.read(buffer);
            if (read == -1) {
                break;
            }
            ostr.write(buffer, 0, read);
        }
        ostr.close();
        message = ostr.toString();
    }
