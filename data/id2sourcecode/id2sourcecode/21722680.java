    private void parseRequest(final HttpServletRequest req) throws IOException {
        String encoding = req.getCharacterEncoding();
        Map<String, String> map = parseHeader(req.getHeader("content-type"));
        String boundary = map.get("boundary");
        if (boundary == null || boundary.length() == 0) return;
        boundary = "" + CR + LF + "--" + boundary;
        if (encoding == null) encoding = DEFAULT_ENCODING;
        final PushbackInputStream input = new PushbackInputStream(new BufferedInputStream(req.getInputStream()), 128);
        unread(LF, input);
        unread(CR, input);
        int c;
        do {
            c = read(input, boundary);
        } while (c != -1);
        while (c != -2) {
            String header = null;
            String type = null;
            String name = null;
            OutputStream out = null;
            File f = null;
            while (!"".equals(header)) {
                header = readLine(input);
                map = parseHeader(header);
                if (map.containsKey("content-disposition")) {
                    name = map.get("name");
                    if (map.containsKey("filename")) {
                        f = new File(map.get("filename"));
                        setFile(f.getName());
                        putParameter(name, f);
                        out = f.getOutputStream();
                    } else out = new ByteArrayOutputStream();
                } else if (map.containsKey("content-type")) {
                    type = map.get("content-type");
                    if (map.containsKey("charset")) encoding = map.get("charset"); else encoding = DEFAULT_ENCODING;
                }
            }
            if (f != null) f.setType(type);
            if (out == null) throw new IOException("Failed to parse the request header");
            while ((c = read(input, boundary)) >= 0) out.write(c);
            out.close();
            if (f == null) putParameter(name, ((ByteArrayOutputStream) out).toString(encoding));
        }
    }
