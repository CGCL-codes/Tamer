    private BlastRequest sendRequest(SequenceI seq, int strand) throws UnsupportedEncodingException, IOException {
        StringBuilder putBuf = new StringBuilder();
        processOptions(putBuf);
        putBuf.append("QUERY=");
        putBuf.append(URLEncoder.encode(">" + seq.getName() + "\n", ENCODING));
        putBuf.append(URLEncoder.encode(strand == 1 ? seq.getResidues() : seq.getReverseComplement(), ENCODING));
        putBuf.append("&DATABASE=nr&");
        putBuf.append("QUERY_BELIEVE_DEFLINE=no&");
        putBuf.append("PROGRAM=" + type.toString() + "&");
        putBuf.append("CMD=Put");
        URL url = new URL(BLAST_URL);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(putBuf.toString());
        wr.flush();
        wr.close();
        BlastRequest req = parseRequest(conn.getInputStream());
        apollo.util.IOUtil.informationDialog("Expected time before analysis starts: " + req.rtoe + " seconds (" + req.rid + ")");
        return req;
    }
