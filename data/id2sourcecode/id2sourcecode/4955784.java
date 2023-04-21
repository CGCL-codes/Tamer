    public void loadCircuit(String fname) {
        try {
            URL url = new URL(getCodeBase() + fname);
            ObjectInputStream s = new ObjectInputStream(url.openStream());
            lsframe.lspanel.gates = (GateList) s.readObject();
            s.close();
        } catch (Exception ex) {
            lsframe.showMessage(ex.toString());
        }
        lsframe.lspanel.gates.reconnect();
        lsframe.lspanel.repaint();
        lsframe.lspanel.changed = false;
    }
