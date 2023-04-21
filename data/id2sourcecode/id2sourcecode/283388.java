            public void run() {
                int fromPort;
                String fromAddr;
                int toPort;
                String toAddr;
                String pipeName = "unknown";
                try {
                    fromPort = m_inSock.getPort();
                    fromAddr = m_inSock.getInetAddress().getHostName();
                    int myInboundPort = m_inSock.getLocalPort();
                    int myOutboundPort = m_outSock.getLocalPort();
                    toPort = m_outSock.getPort();
                    toAddr = m_outSock.getInetAddress().getHostName();
                    m_is = m_inSock.getInputStream();
                    m_os = m_outSock.getOutputStream();
                    pipeName = "[" + fromAddr + ":" + fromPort + "]->[:" + myInboundPort + "->:" + myOutboundPort + "]->[" + toAddr + ":" + toPort + "]";
                    System.out.println("Pipe " + pipeName + " established.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    while (!interrupted()) {
                        int readSize = m_is.read(m_buf);
                        if (readSize <= 0) break;
                        m_os.write(m_buf, 0, readSize);
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                try {
                    m_inSock.shutdownInput();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    m_outSock.shutdownOutput();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.println("Pipe " + pipeName + " discarded.");
            }
