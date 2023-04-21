    private void acceptClientConnections() {
        String name;
        record = localDevice.getRecord(scn);
        record.setDeviceServiceClasses(0x40000);
        UpdateServiceAvail(0);
        while (!stop) {
            try {
                sc = scn.acceptAndOpen();
                if (clients_count > maxClients) continue;
                _rd = RemoteDevice.getRemoteDevice(sc);
                dataIn = sc.openDataInputStream();
                dataOut = sc.openDataOutputStream();
                name = dataIn.readUTF();
                if (findClient(name) != null) {
                    dataOut.writeUTF("name_already_chosen");
                    dataOut.flush();
                    dataOut.close();
                    continue;
                } else {
                    dataOut.writeUTF("send_server_name");
                    dataOut.writeUTF(Media.nick_me);
                    dataOut.flush();
                }
            } catch (IOException ioe) {
                Alert al = new Alert("Error", "IOException: " + ioe.getMessage(), null, AlertType.ERROR);
                menu.FormExchange(-1);
                menu.FormShow(al);
                return;
            }
            UpdateServiceAvail(1);
            setNewClient(dataIn, dataOut, name, _rd.getBluetoothAddress());
            menu.insertNodeNames(getNodeNames());
            Media.ticker.setString("Client connected");
        }
    }
