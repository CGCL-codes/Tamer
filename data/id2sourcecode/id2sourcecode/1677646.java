    public void setConnectionChannelConfig(ConnectionChannelConfig connParam) {
        originalConnParam = connParam;
        jTextFieldChannelName.setText(connParam.getChannelname());
        if (connParam.getParams() == null) {
            jTextFieldTCPIPHostname.setText("<HOST>");
            jTextFieldTCPIPPort.setText("<PORT>");
            jRadioButtonConnTCPIP.setSelected(true);
        } else if (connParam.getClassname().equals(TcpipBoardInterface.class.getName())) {
            if (connParam.getParams().get(0).getName().equals(TcpipBoardInterface.HOSTNAME_CONFIG_NAME)) {
                jTextFieldTCPIPHostname.setText(connParam.getParams().get(0).getValue());
                jTextFieldTCPIPPort.setText(connParam.getParams().get(1).getValue());
            } else {
                jTextFieldTCPIPHostname.setText(connParam.getParams().get(1).getValue());
                jTextFieldTCPIPPort.setText(connParam.getParams().get(2).getValue());
            }
            jRadioButtonConnTCPIP.setSelected(true);
        } else if (connParam.getClassname().equals("de.jlab.communication.rxtx.RxTxBoardInterface")) {
            jComboBoxComPorts.setSelectedItem(connParam.getParams().get(0).getValue());
            jRadioButtonConnRXTX.setSelected(true);
        }
        this.setBorder(BorderFactory.createTitledBorder(connParam.getChannelname()));
    }
