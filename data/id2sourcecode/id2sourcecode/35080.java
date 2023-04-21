    public int get() {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            if (this.address != null) {
                socket = new Socket(this.address, this.port);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                sendGet(reader, writer);
                parseHeaders(reader);
                String html = parseBody(reader);
                storeCache(html);
                if (getContentType().equals("text/html")) {
                    long start = System.currentTimeMillis();
                    parser = new HtmlParser(html);
                    long time = System.currentTimeMillis() - start;
                    System.out.println("#= parse time: " + (((double) time) / 1000.0) + " second(s) =#");
                }
            } else {
                long start = System.currentTimeMillis();
                parser = new HtmlParser(readCache());
                long time = System.currentTimeMillis() - start;
                System.out.println("#= parse time: " + (((double) time) / 1000.0) + " second(s) =#");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (headers != null) {
            return headers.getCode();
        } else {
            return 200;
        }
    }
