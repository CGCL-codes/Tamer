    void broadcastGlobalValues() {
        DatagramConnection dgConnection = null;
        Datagram dg = null;
        try {
            dgConnection = (DatagramConnection) Connector.open("radiogram://broadcast:37");
            dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
        } catch (IOException e) {
            System.out.println("Could not open datagram broadcast connection");
            e.printStackTrace();
            return;
        }
        try {
            dg.reset();
            dg.writeShort(SunSpotAudio.message_globalValues);
            dg.writeFloat(PlanningThread.globalAV.getArousal());
            dg.writeFloat(PlanningThread.globalAV.getValence());
            dgConnection.send(dg);
            dgConnection.close();
            System.out.println("Local AV broadcast is going through");
            signalBroadcast();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
