    public GetOperation run() {
        ServerConnection serverConnection = m_client.getServerConnection("hashKey");
        if (serverConnection == null) {
            m_response = ERROR;
            return (this);
        }
        StringBuilder command = new StringBuilder();
        command.append("get ");
        for (String k : m_keyList) command.append(m_keyEncoder.encodeKey(k)).append(" ");
        command.append("\r\n");
        ByteBuffer[] sendBuffers = new ByteBuffer[1];
        sendBuffers[0] = UTF8.encode(command.toString());
        int bytesToWrite = sendBuffers[0].limit();
        BufferSet bs = m_client.getBufferSet();
        try {
            writeToChannel(serverConnection.getChannel(), sendBuffers, bytesToWrite);
            readResponse(serverConnection, bs, m_timeout, END_OF_GET);
            ByteBufferInputStream is = new ByteBufferInputStream(bs);
            String line = null;
            while ((line = readLine(is)) != null) {
                if (line.startsWith("VALUE")) {
                    String[] split = line.split(" ");
                    String key = split[1];
                    int flags = Integer.parseInt(split[2]);
                    int valueSize = Integer.parseInt(split[3]);
                    m_responseMap.put(key, m_valueEncoder.decodeValue(is, valueSize, flags));
                    if (m_value == null) m_value = m_responseMap.get(key);
                    is.read();
                    is.read();
                }
            }
            serverConnection.recycleConnection();
        } catch (IOException ioe) {
            System.out.println("GetOperation error");
            ioe.printStackTrace();
            serverConnection.closeConnection();
        }
        bs.freeBuffers();
        return (this);
    }
