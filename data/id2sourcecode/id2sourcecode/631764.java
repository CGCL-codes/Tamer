    private void saveFile() {
        if (xmlFile == null) {
            System.err.println("Error: Save File is null\n");
            return;
        }
        fcModel.clearAll();
        preparePanels();
        Iterator panelIter = panels.iterator();
        while (panelIter.hasNext()) {
            FieldConfigPanel fpanel = (FieldConfigPanel) panelIter.next();
            fcModel.addQuanType(fpanel.getQuanType());
        }
        ArrayList chans = FieldConfigPanel.getChannels();
        Iterator chanIter = chans.iterator();
        while (chanIter.hasNext()) {
            Channel chan = (Channel) chanIter.next();
            fcModel.addChannel(chan);
        }
        try {
            fcModel.writeMAGEML(xmlFile);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
