    @Override
    public void valueChanged(ListSelectionEvent e) {
        EventList<EpgEvent> list = model.getSelected();
        if (list.size() > 0) {
            EpgEvent evt = list.get(0);
            channel.setText(evt.getChannel().getName());
            pm.setBean(evt);
            instance = evt;
        }
    }
