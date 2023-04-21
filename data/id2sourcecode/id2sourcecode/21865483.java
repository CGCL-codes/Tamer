    private String printSockets() {
        StringBuffer buffer = new StringBuffer();
        int count = 0;
        Queue clients = GameServer.getInstance().getChannels();
        for (Iterator ir = clients.iterator(); ir.hasNext(); ) {
            SocketChannel client = (SocketChannel) ir.next();
            buffer.append("%$5002|").append(client.socket().getRemoteSocketAddress().toString());
            buffer.append("|");
            buffer.append("0").append("|");
            buffer.append(client.isConnected());
            buffer.append("$!@$");
            count++;
        }
        if (count > 0) return buffer.toString().substring(0, buffer.toString().length() - 4); else return buffer.toString();
    }
