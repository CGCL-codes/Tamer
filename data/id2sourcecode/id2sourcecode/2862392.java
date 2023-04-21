    private void serveLocalRead(String pathname, DataOutputStream out, Socket s) throws IOException {
        File file = fileManager.getRawFile(pathname);
        if (!file.isFile()) {
            sendError("file not found on this server " + pathname, out);
            return;
        }
        out.write(Responses.OK);
        out.writeUTF(file.length() + "");
        out.flush();
        byte[] cached = server.getCache().getCached(pathname);
        if (cached == null) {
            FileInputStream infile = fileManager.openFileRead(pathname);
            try {
                ByteBuffer all = ByteBuffer.allocate((int) file.length());
                infile.getChannel().read(all);
                all.rewind();
                out.write(all.array());
                out.flush();
            } finally {
                infile.close();
            }
        } else {
            out.write(cached);
            out.flush();
        }
    }
