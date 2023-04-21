    public void fillComponents(IrcServerEntry aEditar) {
        if (aEditar == null) {
            cbQuery.setSelected(true);
            cbChannel.setSelected(true);
            cbDCC.setSelected(true);
            channelVector.addElement("#aetheria");
        } else {
            cbQuery.setSelected(aEditar.respondeAPrivados());
            cbChannel.setSelected(aEditar.respondeACanales());
            cbDCC.setSelected(aEditar.respondeADCC());
            for (int i = 0; i < aEditar.getChannels().size(); i++) channelVector.addElement(aEditar.getChannels().get(i));
            serverTextField.setText(aEditar.getServer());
            portTextField.setText(String.valueOf(aEditar.getPort()));
            nickTextField.setText(String.valueOf(aEditar.getNick()));
        }
    }
