    private LinkedList _readHubList(URL an_url) {
        LinkedList ret_val = new LinkedList();
        try {
            URLConnection connection = an_url.openConnection();
            InputStream connectionInputStream = connection.getInputStream();
            if (connectionInputStream != null) if (an_url.getFile().toUpperCase().endsWith(".BZ2")) {
                libLogger.debug("Fetching bzip2 compressed HubList");
                StringBuffer next_line = new StringBuffer();
                int next_char = connectionInputStream.read();
                if (next_char != 'B') {
                    throw new IOException("Invalid bz2 file." + an_url.toString());
                }
                next_char = connectionInputStream.read();
                if (next_char != 'Z') {
                    throw new IOException("Invalid bz2 file." + an_url.toString());
                }
                CBZip2InputStream bz2 = new CBZip2InputStream(connectionInputStream);
                while ((next_char = bz2.read()) != -1) {
                    next_line.append((char) next_char);
                    if (next_char == '\n') {
                        ret_val.addLast(new Hub(next_line.toString()));
                        next_line = new StringBuffer(50);
                    }
                }
                bz2.close();
            } else {
                libLogger.debug("Fetching Std. HubList");
                InputStreamReader isr = new InputStreamReader(connectionInputStream);
                BufferedReader reader = new BufferedReader(isr);
                String next_line = null;
                while ((next_line = reader.readLine()) != null) {
                    ret_val.addLast(new Hub(next_line));
                }
                reader.close();
                isr.close();
            }
            libLogger.debug("Got " + ret_val.size() + " entries.");
        } catch (IOException e) {
            Configuration.instance().executeExceptionCallback(e);
        }
        return ret_val;
    }
