    private String readResponseHeaders(InputStream inp) throws IOException {
        if (buf_pos == 0) Log.write(Log.RESP, "Resp:  Reading Response headers " + inp_stream.hashCode()); else Log.write(Log.RESP, "Resp:  Resuming reading Response headers " + inp_stream.hashCode());
        if (!reading_lines) {
            try {
                if (buf_pos == 0) {
                    int c;
                    do {
                        if ((c = inp.read()) == -1) throw new EOFException("Encountered premature EOF " + "while reading Version");
                    } while (Character.isWhitespace((char) c));
                    buf[0] = (byte) c;
                    buf_pos = 1;
                }
                while (buf_pos < buf.length) {
                    int got = inp.read(buf, buf_pos, buf.length - buf_pos);
                    if (got == -1) throw new EOFException("Encountered premature EOF " + "while reading Version");
                    buf_pos += got;
                }
            } catch (EOFException eof) {
                Log.write(Log.RESP, "Resp:  (" + inp_stream.hashCode() + ")", eof);
                throw eof;
            }
            for (int idx = 0; idx < buf.length; idx++) hdrs.append((char) buf[idx]);
            reading_lines = true;
        }
        if (hdrs.toString().startsWith("HTTP/") || hdrs.toString().startsWith("HTTP ")) readLines(inp);
        buf_pos = 0;
        reading_lines = false;
        bol = true;
        got_cr = false;
        String tmp = hdrs.toString();
        hdrs.setLength(0);
        return tmp;
    }
