    protected OutputStream postConnection(int command) {
        OutputStream out = null;
        try {
            URL url = new URL(_saddr);
            _serverCon = url.openConnection();
            _serverCon.setDoOutput(true);
            _serverCon.setDoInput(true);
            out = _serverCon.getOutputStream();
            PrintWriter pw = new PrintWriter(out);
            pw.print(command + "\n");
            pw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return out;
    }
