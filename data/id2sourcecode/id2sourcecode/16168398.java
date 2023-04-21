    public void KBstoreModel(int cmd) throws IOException {
        OutputStream out = postConnection(cmd);
        if (_MDRsupported) {
            MDRmanager.exportXMI(_refp, out);
        } else {
            _buffer.writeTo(out);
        }
        out.flush();
        out.close();
        InputStream in = getResponse();
        int x;
        while ((x = in.read()) != -1) System.out.write(x);
        System.out.flush();
    }
