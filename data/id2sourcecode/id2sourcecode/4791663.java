    private void send(InputStream in, long size) throws IOException {
        in = new BufferedInputStream(in);
        byte[] buffer = new byte[1024 * 100];
        long tot = 0;
        ioProgress(CMD_SEND_FILE, tot, size, ProgressSource.State.NEW);
        while (true) {
            int read = in.read(buffer);
            if (read < 0) break;
            os.write(buffer, 0, read);
            tot += read;
            ioProgress(CMD_SEND_FILE, tot, size, ProgressSource.State.UPDATE);
        }
        in.close();
        os.flush();
        ioProgress(CMD_SEND_FILE, tot, size, ProgressSource.State.DELETE);
        if (debug) System.out.println("Sent " + tot + "/" + size + " bytes");
        checkResponse();
    }
