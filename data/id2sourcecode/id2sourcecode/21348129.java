    @Override
    public int execute(Session session, SqshOptions opts) throws Exception {
        Options options = (Options) opts;
        String readBuffer = "!.";
        String writeBuffer = "!.";
        if (options.arguments.size() > 2) {
            session.err.println("Use: \buf-edit [read-buf [write-buf]]");
            return 1;
        }
        BufferManager bufMan = session.getBufferManager();
        if (options.arguments.size() == 0) {
            Buffer current = bufMan.getCurrent();
            if (current.getLineNumber() == 1 && bufMan.getBuffer("!..") != null) {
                readBuffer = "!..";
            }
        } else {
            readBuffer = options.arguments.get(0);
            if (options.arguments.size() == 2) {
                writeBuffer = options.arguments.get(1);
            }
        }
        Buffer readBuf = bufMan.getBuffer(readBuffer);
        if (readBuf == null) {
            session.err.println("Buffer '" + readBuffer + "' does not exist");
            return 1;
        }
        Buffer writeBuf = bufMan.getBuffer(writeBuffer);
        if (writeBuf == null) {
            session.err.println("Buffer '" + writeBuffer + "' does not exist");
            return 1;
        }
        String editor = getEditor(session);
        File tmpFile = null;
        Shell s = null;
        try {
            tmpFile = File.createTempFile("jsqsh", ".sql");
            readBuf.save(tmpFile);
            s = session.getShellManager().detachShell(editor + " " + tmpFile.toString());
            try {
                s.waitFor();
            } catch (InterruptedException e) {
            }
            writeBuf.load(tmpFile);
        } catch (IOException e) {
            session.err.println(e.toString());
        } finally {
            if (tmpFile != null) {
                tmpFile.delete();
            }
        }
        if (writeBuf == bufMan.getCurrent()) {
            throw new SessionRedrawBufferMessage();
        }
        return 0;
    }
