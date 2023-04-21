    void sendAPDU() throws CardException {
        CommandAPDU apdu = null;
        if (!SC.isConnected()) {
            this.reportException(new Exception("Please connect first"));
            return;
        }
        String Sdata = tbAPDUDatain.getText().trim();
        String Scla = tbAPDUClass.getText().trim();
        String Sins = tbAPDUIns.getText().trim();
        String Sp1 = tbAPDUP1.getText().trim();
        String Sp2 = tbAPDUP2.getText().trim();
        String Sp3 = tbAPDUP3.getText().trim();
        String Sle = tbAPDULe.getText().trim();
        Integer cla = StringUtil.parseHex(Scla);
        Integer ins = StringUtil.parseHex(Sins);
        Integer p1 = StringUtil.parseHex(Sp1);
        Integer p2 = StringUtil.parseHex(Sp2);
        Integer p3 = StringUtil.parseHex(Sp3);
        Integer le = StringUtil.parseHex(Sle);
        if (cla == null) {
            this.reportException(new Exception("Invalid value for CLASS"));
            return;
        }
        if (ins == null) {
            this.reportException(new Exception("Invalid value for INS"));
            return;
        }
        if (p1 == null || p2 == null) {
            this.reportException(new Exception("Invalid value for P1/2"));
            return;
        }
        byte[] data = StringUtil.stringToByteArr(Sdata);
        if (Sdata.length() == 0 && le == null) apdu = new CommandAPDU(cla, ins, p1, p2); else if (p3 == null && le == null) apdu = new CommandAPDU(cla, ins, p1, p2, data); else if (le == null) apdu = new CommandAPDU(cla, ins, p1, p2, data, 0, p3); else {
            if (p3 == null) p3 = data.length;
            apdu = new CommandAPDU(cla, ins, p1, p2, data, 0, p3, le);
        }
        ResponseAPDU res = SC.getChannel().transmit(apdu);
        String sw = StringUtil.byteToHex(res.getSW1()) + " " + StringUtil.byteToHex(res.getSW2());
        ISO7816Response isoResp = new ISO7816Response(res.getSW1(), res.getSW2());
        String status = isoResp.toString();
        if (isoResp.isGood()) lbLastStatus.setForeground(Color.GREEN); else lbLastStatus.setForeground(Color.RED);
        lbLastStatus.setText(status);
        String dataoutHex = StringUtil.byteArrToString(res.getData(), " ");
        String dataoutStr = StringUtil.byteArrToPrintableString(res.getData());
        String fullResHex = StringUtil.byteArrToString(res.getBytes(), " ");
        DefaultTableModel table = (DefaultTableModel) tabAPDU.getModel();
        table.addRow(new Object[] { Scla, Sins, Sp1, Sp2, Sp3, Sle, Sdata, sw, status, dataoutHex, dataoutStr, fullResHex });
        table.fireTableDataChanged();
    }
