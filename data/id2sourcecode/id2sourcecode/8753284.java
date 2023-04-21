    @SuppressWarnings("unchecked")
    public T run() {
        int flags = 0;
        String resp = NOT_STORED;
        String key = m_keyEncoder.encodeKey(m_key);
        ServerConnection serverConnection = m_client.getServerConnection(m_hashKey);
        if (serverConnection == null) {
            m_response = ERROR;
            return ((T) this);
        }
        BufferSet bs = m_client.getBufferSet();
        m_valueEncoder.encodeValue(m_value, new ByteBufferOutputStream(bs));
        bs.flipBuffers();
        List<ByteBuffer> bufferList = bs.getBuffers();
        int bufferCount = bufferList.size() + 2;
        ByteBuffer[] sendBuffers = new ByteBuffer[bufferCount];
        int dataSize = 0;
        for (int I = 1; I < sendBuffers.length - 1; I++) {
            sendBuffers[I] = bufferList.get(I - 1);
            dataSize += sendBuffers[I].limit();
        }
        StringBuilder command = new StringBuilder();
        command.append(getOperation()).append(" ");
        command.append(key).append(" ");
        command.append(flags).append(" ");
        command.append(m_expiry).append(" ");
        command.append(dataSize).append(" ");
        if (!m_reply) command.append("noreply");
        command.append("\r\n");
        sendBuffers[0] = UTF8.encode(command.toString());
        sendBuffers[bufferCount - 1] = ByteBuffer.wrap(END_OF_LINE);
        int bytesToWrite = sendBuffers[0].limit();
        bytesToWrite = dataSize + END_OF_LINE.length;
        try {
            writeToChannel(serverConnection.getChannel(), sendBuffers, bytesToWrite);
            bs.freeBuffers();
            if (m_reply) {
                readResponse(serverConnection, bs, m_timeout, END_OF_LINE);
                String line = readLine(new ByteBufferInputStream(bs));
                if (line != null) resp = line;
            } else resp = STORED;
            serverConnection.recycleConnection();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            serverConnection.closeConnection();
        }
        bs.freeBuffers();
        m_response = resp;
        return ((T) this);
    }
