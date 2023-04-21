    protected boolean handleFingerprint(byte[] message, byte fingerprint_type) {
        if (fingerprint_type != EventManager.MESSAGE_TYPE_FINGERPRINT_MEAN_GZIP && fingerprint_type != EventManager.MESSAGE_TYPE_FINGERPRINT_STDEV_GZIP) {
            System.err.println("Invalid fingerprint type received.  Ignoring data.");
            return false;
        }
        ByteArrayOutputStream uncompressed = new ByteArrayOutputStream();
        try {
            GZIPInputStream unzipStream = new GZIPInputStream(new ByteArrayInputStream(message));
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = unzipStream.read(buffer, 0, buffer.length)) > 0) {
                uncompressed.write(buffer, 0, read);
            }
        } catch (IOException ioe) {
            System.err.println("Couldn't decompress fingerprint.");
            return false;
        }
        if (uncompressed.size() == 0) {
            System.err.println("No data decompressed.");
            return false;
        }
        DataInputStream message_stream = new DataInputStream(new ByteArrayInputStream(uncompressed.toByteArray()));
        try {
            while (message_stream.available() > 0) {
                int region_id = message_stream.readInt();
                MACAddress device_mac = MACAddress.getMACAddress(message_stream.readLong());
                int phy = message_stream.readInt();
                long timestamp = message_stream.readLong();
                int total_landmarks = message_stream.readInt();
                TransmitterInfo transmitterInfo = this.serverInfo.transmitterInfo.get(device_mac);
                if (transmitterInfo == null) {
                    transmitterInfo = new TransmitterInfo(device_mac, phy);
                    this.serverInfo.transmitterInfo.put(device_mac, transmitterInfo);
                }
                byte decompressedType = fingerprint_type == EventManager.MESSAGE_TYPE_FINGERPRINT_MEAN_GZIP ? EventManager.MESSAGE_TYPE_FINGERPRINT_MEAN : EventManager.MESSAGE_TYPE_FINGERPRINT_STDEV;
                FingerprintInfo fingerprintInfo = new FingerprintInfo(transmitterInfo, decompressedType, -1l, -1l);
                int landmarks = 0;
                while (landmarks++ < total_landmarks) {
                    MACAddress hub_mac = MACAddress.getMACAddress(message_stream.readLong());
                    int hub_phy = message_stream.readInt();
                    int antenna = message_stream.readInt();
                    float rssi = message_stream.readFloat();
                    HubInfo temp_hub = this.serverInfo.hubInfo.get(hub_mac);
                    if (temp_hub == null) {
                        System.err.println("Couldn't find hub " + hub_mac.toString());
                        return true;
                    }
                    LandmarkInfo temp_landmark = temp_hub.getLandmarkInfo(hub_phy, antenna);
                    if (temp_landmark == null) {
                        System.err.println("Couldn't find landmark " + hub_mac.toString() + "/" + hub_phy + "/" + antenna);
                        return true;
                    }
                    fingerprintInfo.setRSSIValue(temp_landmark, rssi);
                }
                transmitterInfo.setFingerprintInfo(fingerprintInfo);
                if (fingerprintInfo.size() != 0) this.fireServerEvent(new FingerprintEvent(this, fingerprintInfo));
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getLocalizedMessage());
            ioe.printStackTrace();
            System.err.println("Bad fingerprint data.");
            return false;
        }
        return true;
    }
