    public void postShowChange() {
        context.getShow().getDimmers().addNameListener(this);
        context.getShow().getChannels().addNameListener(this);
        fireTableDataChanged();
    }
