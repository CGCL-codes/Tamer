    void deliverSpecial() throws IOException {
        requestedFile = requestedFile.substring(2);
        if (requestedFile.equalsIgnoreCase("index.html")) {
            deliverIndex();
            return;
        } else if (requestedFile.equalsIgnoreCase("tree.html")) {
            deliverTree();
            return;
        } else if (requestedFile.equalsIgnoreCase("search.html")) {
            deliverSearch();
            return;
        }
        File f = new File("template/" + requestedFile);
        if (!f.canRead()) {
            sendResponseHeader("text/plain");
            sendString("404: not found: " + f.getAbsolutePath());
            return;
        }
        int indexDot = requestedFile.indexOf(".");
        if (indexDot == -1) indexDot = 0;
        String ext = requestedFile.substring(indexDot, requestedFile.length());
        sendResponseHeader(loopupMime(ext));
        RandomAccessFile rf = new RandomAccessFile("template/" + requestedFile, "r");
        ByteBuffer in = rf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, rf.length());
        byte[] outbuf = new byte[(int) rf.length()];
        in.get(outbuf);
        out.write(outbuf);
    }
