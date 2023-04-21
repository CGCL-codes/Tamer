            public Connection(SockIOPool.SockIO sock, StringBuilder request) throws IOException {
                if (log.isDebugEnabled()) log.debug("setting up connection to " + sock.getHost());
                this.sock = sock;
                outgoing = ByteBuffer.wrap(request.append("\r\n").toString().getBytes());
                channel = sock.getChannel();
                if (channel == null) throw new IOException("dead connection to: " + sock.getHost());
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_WRITE, this);
            }
