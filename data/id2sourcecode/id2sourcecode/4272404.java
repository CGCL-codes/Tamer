    public void removePainel(Channel c) {
        Component[] components = painelCentral.getComponents();
        for (Component comp : components) {
            if (comp instanceof PainelCanal) {
                if (((PainelCanal) comp).getCanal() == c) {
                    painelCentral.remove(comp);
                    abas.remove(comp);
                    Client.getInstance().getChannels().removerCanal(c);
                }
            }
        }
    }
