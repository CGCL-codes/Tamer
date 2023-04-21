    private NsclientPacket sendCheckRequest(String request) throws NsclientException {
        byte[] buffer = new byte[1024];
        m_ByteArrayOutStream.reset();
        try {
            m_Socket.getOutputStream().write(request.getBytes());
            m_Socket.getOutputStream().flush();
            int read = m_BufInStream.read(buffer);
            if (read > 0) m_ByteArrayOutStream.write(buffer, 0, read);
            return new NsclientPacket(m_ByteArrayOutStream.toString());
        } catch (Exception e) {
            throw new NsclientException("Unknown exception: " + e.getMessage(), e);
        }
    }
