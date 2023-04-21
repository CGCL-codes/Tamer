    private void selectTemplate(Template t) {
        selectedTemplate = t;
        if (t == null) {
            boundChannelListModel.clear();
            boundChannelList.setEnabled(false);
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(false);
            enableSeriesWidgets(false);
            sliderPanel.setStrip(null);
            Strip tempStrip = selectedStrip;
            selectedStrip = null;
            templateBox.setSelectedIndex(-1);
            selectedStrip = tempStrip;
            return;
        }
        if (templateBox.getSelectedItem() != t) {
            ItemListener[] listeners = templateBox.getItemListeners();
            templateBox.removeItemListener(listeners[0]);
            templateBox.setSelectedItem(t);
            templateBox.addItemListener(listeners[0]);
        }
        selectedStrip.setTemplate(t);
        boundChannelListModel.removeAllElements();
        List<Crayon> crayonList = t.getCrayons();
        int count = 0;
        while (count < crayonList.size()) {
            StripChannelBinding binding = selectedStrip.getChannelBinding(count);
            boundChannelListModel.addElement(binding);
            count++;
        }
        sliderPanel.setStrip(selectedStrip);
        sliderPanel.repaint();
        enableStripWidgets(true);
        enableCardinalWidgets(true);
    }
