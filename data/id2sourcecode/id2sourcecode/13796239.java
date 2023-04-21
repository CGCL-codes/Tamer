    public void send() throws IOException {
        if (!mayfilter) {
            super.send();
            return;
        }
        byte[] v = new byte[2048];
        int read;
        List<HTMLFilter> filters = initFilters();
        HTMLParser parser = new HTMLParser();
        HTMLBlock block = null;
        long total = 0;
        int start = 0;
        int len = v.length;
        while ((size < 0 || total < size) && (read = contentstream.read(v, start, len)) > 0) {
            total += read;
            read += start;
            parser.setText(v, read);
            try {
                block = parser.parse();
                int fsize = filters.size();
                for (int i = 0; i < fsize; i++) {
                    HTMLFilter hf = filters.get(i);
                    hf.filterHTML(block);
                }
                block.send(clientstream);
            } catch (HTMLParseException e) {
                logError(Logger.INFO, "Bad HTML: " + e.toString());
                clientstream.write(v, 0, read);
            }
            if (block != null && block.restSize() > 0) {
                start = block.restSize();
                if (start == v.length) v = new byte[v.length + 2048];
                len = v.length - start;
                block.insertRest(v);
            } else {
                start = 0;
                len = v.length;
            }
            if (size > -1) {
                long l = size - total;
                if (l < len) len = (int) l;
            }
        }
        if (block != null) {
            block.sendRest(clientstream);
        }
        if (size > 0 && total != size) setPartialContent(total, size);
    }
