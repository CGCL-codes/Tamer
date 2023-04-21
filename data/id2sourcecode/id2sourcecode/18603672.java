    public DeleteOperation run() {
        String resp = NOT_FOUND;
        String key = m_keyEncoder.encodeKey(m_key);
        ServerConnection serverConnection = m_client.getServerConnection(m_hashKey);
        if (serverConnection == null) {
            m_response = ERROR;
            return (this);
        }
        StringBuilder command = new StringBuilder();
        command.append("delete ");
        command.append(key).append(" ");
        if (!m_reply) command.append("noreply");
        command.append("\r\n");
        ByteBuffer[] sendBuffers = new ByteBuffer[1];
        sendBuffers[0] = UTF8.encode(command.toString());
        int bytesToWrite = sendBuffers[0].limit();
        BufferSet bs = m_client.getBufferSet();
        try {
            writeToChannel(serverConnection.getChannel(), sendBuffers, bytesToWrite);
            if (m_reply) {
                readResponse(serverConnection, bs, m_timeout, END_OF_LINE);
                String line = readLine(new ByteBufferInputStream(bs));
                if (line != null) resp = line;
            } else resp = DELETED;
            serverConnection.recycleConnection();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            serverConnection.closeConnection();
        }
        bs.freeBuffers();
        m_response = resp;
        return (this);
    }
