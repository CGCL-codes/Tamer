    private void getServersButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String ipstr = ipField.getText();
        serverDets = GrabChannelData.getChannelData(ipstr);
        if (serverDets == null) {
            System.out.println("HUH HUH HUH");
            descriptionBox.setText("No WITNA channel found at " + ipstr + ".");
            serverDets = new String[0][8];
            setServersTableModel();
            serversTable.setModel(serversModel);
            setServersTableColumns();
            return;
        }
        setServersTableModel();
        serversTable.setModel(serversModel);
        setServersTableColumns();
        if (serverDets.length == 0) {
            descriptionBox.setText("No servers currently connected to channel " + ipstr + ".");
        } else {
            for (int x = 0; x < serverDets.length; x++) {
                for (int y = 0; y < 4; y++) {
                    serversTable.getModel().setValueAt(serverDets[x][y], x, y);
                }
                for (int y = 5; y < 8; y++) {
                    serversTable.getModel().setValueAt(serverDets[x][y], x, y - 1);
                }
            }
        }
    }
