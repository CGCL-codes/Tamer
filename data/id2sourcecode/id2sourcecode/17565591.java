    private void copyData(URL url, FileOutputStream out) throws IOException {
        InputStream in = url.openConnection().getInputStream();
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buf)) != -1) out.write(buf, 0, bytesRead);
        out.write('\n');
        in.close();
    }
