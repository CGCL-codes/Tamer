    public boolean Upload(String filename, String destination, int type, boolean process) {
        Socket dataSocket;
        String address, host = null;
        int port;
        os.println("pasv");
        address = GetReplyString(is);
        address = address.substring(address.indexOf('(') + 1, address.indexOf(')'));
        StringTokenizer t = new StringTokenizer(address, ",");
        for (int i = 0; i < 4; i++) {
            if (host == null) host = t.nextToken(); else host += "." + t.nextToken();
        }
        port = Integer.parseInt(t.nextToken()) << 8;
        port += Integer.parseInt(t.nextToken());
        try {
            dataSocket = new Socket(host, port);
        } catch (IOException e) {
            System.err.println("Could not connect to server, " + e);
            return (false);
        }
        if (type == ASCII) os.println("type a"); else os.println("type i");
        GetReply(is);
        os.println("stor " + destination);
        int result = GetReply(is);
        if (result == PRELIM) {
            try {
                OutputStream out = dataSocket.getOutputStream();
                byte buffer[] = new byte[1024];
                RandomAccessFile in = new RandomAccessFile(filename, "r");
                String line;
                int amount;
                if (type == ASCII && process) {
                    while ((line = in.readLine()) != null) {
                        line = callback.processData(line);
                        line.getBytes(0, line.length(), buffer, 0);
                        out.write(buffer, 0, line.length());
                        out.write('\n');
                    }
                } else {
                    while ((amount = in.read(buffer)) > 0) out.write(buffer, 0, amount);
                }
                in.close();
                out.close();
                dataSocket.close();
                result = GetReply(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (result == COMPLETE);
        } else {
            return (false);
        }
    }
