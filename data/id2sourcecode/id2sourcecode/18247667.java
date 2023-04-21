    public InputStream getInputStream() throws IOException {
        PipedOutputStream pos = new PipedOutputStream();
        Runnable writer;
        if (message == null) writer = new FolderWriter(folder, pos); else writer = new MessageWriter(message, pos);
        Thread thread = new Thread(writer, "MailboxURLConnection.getInputStream");
        thread.start();
        return new PipedInputStream(pos);
    }
