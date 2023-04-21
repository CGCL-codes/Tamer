    public Channel getChannel() {
        Channel ret = null;
        String name = nameTextField.getText();
        String bouqet = bouqetTextField.getText();
        String nameBouqet = name;
        if (!Utils.isEmpty(bouqet)) {
            nameBouqet += ";" + bouqet;
        }
        ret = new Channel(nameBouqet);
        ret.setFrequenz(frequenzTextField.getText());
        ret.setParameter(parameterTextField.getText());
        ret.setSource(sourceTextField.getText());
        ret.setSymbolrate(symbolrateTextField.getText());
        ret.setVPid(vpidTextField.getText());
        ret.setAPid(apidTextField.getText());
        ret.setTPid(tpidTextField.getText());
        ret.setCaId(caidTextField.getText());
        ret.setSid(sidTextField.getText());
        ret.setNid(nidTextField.getText());
        ret.setTid(tidTextField.getText());
        ret.setRid(ridTextField.getText());
        return ret;
    }
