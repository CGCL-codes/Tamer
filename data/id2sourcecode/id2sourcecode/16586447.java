    public synchronized void connectionClosed(Interface thread) {
        IntNetwork.getInstance().removeDevice(thread.remote_addr);
        if (interfaces.contains(thread)) {
            debug.writeLine("disconnect " + thread.remote_addr);
            int importance = thread.reachable.length;
            for (int i = 0; i < thread.reachable.length; i++) {
                IntNetwork.getInstance().removeDevice(thread.reachable[i]);
            }
            interfaces.removeElement(thread);
            server.updateNumConnections(interfaces.size());
            if ((importance > 1) || (interfaces.size() == 0)) if (connector != null) connector.fastReconnect();
            Interface[] if_list = new Interface[interfaces.size()];
            interfaces.copyInto(if_list);
            for (int i = 0; i < if_list.length; i++) if_list[i].send(PacketFactory.createRoutePacket(PacketFactory.PACKET_ROUTE_DEL, thread.reachable));
        }
    }
