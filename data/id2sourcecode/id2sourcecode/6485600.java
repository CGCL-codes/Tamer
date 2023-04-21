    private Vector<Channel> getChannelInfo() {
        Vector<Channel> cList = null;
        Socket socket = null;
        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            socket = new Socket(serverAddress, serverQueryPort);
            socket.setReceiveBufferSize(4069);
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line = in.readLine();
            while (line != null) {
                if (line.equals("[TS]")) {
                    out.write("cl " + serverUDPPort + "\nquit\n");
                    out.flush();
                    line = in.readLine();
                    line = in.readLine();
                    StringTokenizer tokenizer;
                    cList = new Vector<Channel>();
                    while ((line != null) && (!line.equals("OK"))) {
                        tokenizer = new StringTokenizer(line, "	");
                        while (tokenizer.hasMoreTokens()) {
                            cList.add(new Channel());
                            cList.lastElement().setId(Integer.parseInt(tokenizer.nextToken()));
                            cList.lastElement().setCodec(Integer.parseInt(tokenizer.nextToken()));
                            cList.lastElement().setCodecStr(getVerboseCodec(cList.lastElement().getCodec()));
                            cList.lastElement().setParent(Integer.parseInt(tokenizer.nextToken()));
                            cList.lastElement().setOrder(Integer.parseInt(tokenizer.nextToken()));
                            cList.lastElement().setMaxusers(Integer.parseInt(tokenizer.nextToken()));
                            cList.lastElement().setName(deleteQuotes(tokenizer.nextToken()));
                            cList.lastElement().setFlags(Integer.parseInt(tokenizer.nextToken()));
                            cList.lastElement().setAttributes(this.getChannelFlags(cList.lastElement().getFlags()));
                            cList.lastElement().setPassword(tokenizer.nextToken());
                            cList.lastElement().setTopic(deleteQuotes(tokenizer.nextToken()));
                            line = in.readLine();
                        }
                    }
                }
                line = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(cList);
        return cList;
    }
