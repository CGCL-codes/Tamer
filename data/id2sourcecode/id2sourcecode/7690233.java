    public void run() {
        Date sampleTime = new Date((new Date()).getTime() - m_delayPeriod);
        Vector values = new Vector();
        String sensorname = "Synchronizer";
        String[] channels = new String[0];
        Object[] queues = m_channels.values().toArray();
        for (int i = 0; i < queues.length; i++) {
            PacketAligner.realign(((PacketQueue) queues[i]).getAllPackets());
            Object[] objpkts = ((PacketQueue) queues[i]).peekN(2);
            BTDataPacket[] pkts = new BTDataPacket[2];
            pkts[0] = (BTDataPacket) objpkts[0];
            pkts[1] = (BTDataPacket) objpkts[1];
            if (pkts[0] == null) {
                System.out.println("ERROR: Synchronizer: Empty queue found!");
                continue;
            }
            String[] newChannels = new String[channels.length + pkts[0].getChannels().length];
            for (int j = channels.length; j < newChannels.length; j++) {
                newChannels[j] = pkts[0].getSensorname() + "_" + pkts[0].getChannels()[j - channels.length];
            }
            channels = newChannels;
            while (true) {
                if (pkts[1] == null || sampleTime.getTime() <= pkts[0].getTimestamp()) {
                    values.addAll(pkts[0].getValues());
                    break;
                } else if (pkts[0].getTimestamp() < sampleTime.getTime() && sampleTime.getTime() < pkts[1].getTimestamp()) {
                    values.addAll(subsample(sampleTime.getTime(), pkts[0], pkts[1]));
                    break;
                }
                ((PacketQueue) queues[i]).poll();
                objpkts = ((PacketQueue) queues[i]).peekN(2);
                pkts = new BTDataPacket[2];
                pkts[0] = (BTDataPacket) objpkts[0];
                pkts[1] = (BTDataPacket) objpkts[1];
            }
        }
        BTDataPacket outPacket = new BTDataPacket(sampleTime, values, sensorname, channels);
        m_obs.reportValues(outPacket);
    }
