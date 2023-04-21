    private boolean handleRequest(RequestAdapter req, ResponseAdapter res, Socket fcgiSocket, OutputStream out, boolean keepalive) throws IOException {
        OutputStream ws = fcgiSocket.getOutputStream();
        writeHeader(ws, FCGI_BEGIN_REQUEST, 8);
        int role = FCGI_RESPONDER;
        ws.write(role >> 8);
        ws.write(role);
        ws.write(keepalive ? FCGI_KEEP_CONN : 0);
        for (int i = 0; i < 5; i++) ws.write(0);
        setEnvironment(ws, req);
        InputStream in = req.getInputStream();
        byte[] buf = new byte[4096];
        int len = buf.length;
        int sublen;
        writeHeader(ws, FCGI_PARAMS, 0);
        boolean hasStdin = false;
        while ((sublen = in.read(buf, 0, len)) > 0) {
            hasStdin = true;
            writeHeader(ws, FCGI_STDIN, sublen);
            ws.write(buf, 0, sublen);
        }
        if (hasStdin) {
            writeHeader(ws, FCGI_STDIN, 0);
        }
        FastCGIInputStream is = new FastCGIInputStream(fcgiSocket);
        int ch = parseHeaders(res, is);
        if (ch >= 0) out.write(ch);
        while ((ch = is.read()) >= 0) out.write(ch);
        return !is.isDead() && keepalive;
    }
