        private void processFoundServices() {
            String[] list = new String[0];
            for (int curDevice = 0; curDevice < foundDevices.length; curDevice++) {
                if (foundDevices[curDevice] == null || foundServices[curDevice] == null) continue;
                String serviceUrl = null, serverID = null;
                int channel = -1;
                try {
                    serverID = foundDevices[curDevice].getFriendlyName(false);
                    if (serverID == null) serverID = foundDevices[curDevice].getBluetoothAddress();
                } catch (Exception ee) {
                }
                if (serverID == null) serverID = "unkown";
                for (int i = 0; i < foundServices[curDevice].length; i++) {
                    ServiceRecord sr = foundServices[curDevice][i];
                    DataElement plat = attrs != null ? sr.getAttributeValue(attrs[0]) : null;
                    if (plat != null) debug.append(";pf"); else debug.append(";p0");
                    if (plat != null || serviceUrl == null) serviceUrl = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    if (plat != null || channel < 0) channel = getChannel(sr, -1);
                    if (searchMle && plat != null) {
                        Enumeration eplat = (Enumeration) plat.getValue();
                        while (eplat.hasMoreElements()) {
                            DataElement p1 = (DataElement) eplat.nextElement();
                            Enumeration eplat2 = (Enumeration) p1.getValue();
                            while (eplat2.hasMoreElements()) {
                                DataElement p2 = (DataElement) eplat2.nextElement();
                                String v = (String) p2.getValue();
                                char c = v.charAt(0);
                                v = v.substring(1);
                                if (c == 'p') platformIDs = HelperStd.incArray(platformIDs, v);
                                if (c == 'u') {
                                    usernames = HelperStd.incArray(usernames, v);
                                    connectionUrl = HelperStd.incArray(connectionUrl, serviceUrl);
                                    list = HelperStd.incArray(list, v + (serverID != null ? " (" + serverID + ")" : ""));
                                }
                            }
                        }
                    }
                }
                if (serviceUrl != null) foundChannels = HelperStd.incArray(foundChannels, channel);
                if ((connectionOpen || allDevices) && serviceUrl != null) {
                    serverIDs = HelperStd.incArray(serverIDs, serverID);
                    connectionUrl = HelperStd.incArray(connectionUrl, serviceUrl);
                    list = HelperStd.incArray(list, serverID);
                }
            }
            deviceList.deleteAll();
            if (list.length <= 0) {
                deviceList.append("No device found", null);
                HelperApp.setInfoAlert(debug.toString(), "search finished", deviceList);
            } else {
                if (searchMle) {
                    deviceList.addCommand(sendMsg);
                    deviceList.addCommand(userDetail);
                } else deviceList.addCommand(cmdSelect);
                for (int i = 0; i < list.length; i++) {
                    if (list[i] != null) deviceList.append(list[i], HelperApp.getSystemImage(HelperApp.IMAGE_APP_MAIL));
                }
            }
            stopInquirey();
        }
