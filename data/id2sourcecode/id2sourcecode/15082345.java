    public void read(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        ByteBuffer bb = in.getChannel().map(MapMode.READ_ONLY, 0, 1024 * 64);
        bb.order(ByteOrder.nativeOrder());
        int nchars = bb.getInt();
        byte[] byteString = new byte[nchars];
        bb.get(byteString);
        String headerString = new String(byteString);
        while (true) {
            nchars = bb.getInt();
            byteString = new byte[nchars];
            bb.get(byteString);
            headerString = new String(byteString);
            if (headerString.equals("HEADER_END")) break;
            HeaderID headerID = null;
            try {
                headerID = HeaderID.valueOf(headerString);
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Unknown Sigproc Header Element: " + headerString);
                continue;
            }
            this.parseHeaderElem(headerID, bb);
        }
        this.setHeaderLength(bb.position());
        in.close();
        this.written = true;
    }
