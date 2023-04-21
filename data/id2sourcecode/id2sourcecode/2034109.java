    private void jbNewSshServerActionPerformed(ActionEvent evt) {
        int ret = -1;
        if (jcbSshServers.getItemCount() == 0) {
            String[] options = { "New", "Cancel" };
            ret = JOptionPane.showOptionDialog(this, "Create a new Ssh Server configuration ...", "New Ssh Server configuration", 0, JOptionPane.QUESTION_MESSAGE, null, options, null);
            ret += 1;
        } else {
            String[] options = { "Copy", "New", "Cancel" };
            ret = JOptionPane.showOptionDialog(this, "What kind of new configuration ?\n- a copy of the current configuration\n- a totally new one\n- cancel this creation", "New Ssh Server configuration", 0, JOptionPane.QUESTION_MESSAGE, null, options, null);
        }
        SshServerInfo ssi = null;
        switch(ret) {
            case 0:
                ssi = new SshServerInfo();
                ssi.readXML(wsm.getActiveSshServer().writeXML());
                ssi.setAccountAlias("Copy of " + ssi.getAccountAlias());
                break;
            case 1:
                ssi = new SshServerInfo();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                ssi.setAccountAlias("Config" + sdf.format(new Date()));
                break;
            default:
                break;
        }
        if (ssi != null) {
            ArrayList<SshServerInfo> sshServers = wsm.getSshServers();
            sshServers.add(ssi);
            wsm.setSshServers(sshServers);
            wsm.setActiveServerIndex(sshServers.size() - 1);
            jcbSshServers.addItem(ssi);
            jcbSshServers.setSelectedIndex(wsm.getActiveServerIndex());
            if (sshServers.size() == 1) setSshServerConfigEnabled(true);
        }
        this.repaint();
    }
