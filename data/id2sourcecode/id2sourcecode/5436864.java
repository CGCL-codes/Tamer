    public static void broadcastLocalAV() {
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
            dg.writeShort(SunSpotAudio.message_localAV);
            dg.writeFloat(PlanningThread.localAV.getArousal());
            dg.writeFloat(PlanningThread.localAV.getValence());
            dgConnection.send(dg);
            dgConnection.close();
            System.out.println("Local AV broadcast is going through");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
