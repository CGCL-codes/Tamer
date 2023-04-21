    public void actionPerformed(ActionEvent event) {
        ConsoleSettings cs = _console.getSettings();
        if (event.getSource().equals(_apply)) {
            int perLine = Integer.parseInt(_perLine.getText());
            int hGrouping = Integer.parseInt(_hGrouping.getText());
            int vGrouping = Integer.parseInt(_vGrouping.getText());
            ACTSettingsShowChannelMod mod = new ACTSettingsShowChannelMod();
            if (perLine != _initPerLine) mod.setPerLine(perLine);
            if (hGrouping != _initHGrouping) mod.setPerHGroup(hGrouping);
            if (vGrouping != _initVGrouping) mod.setPerVGroup(vGrouping);
            _actInt.interprete(mod);
            cs = _console.getSettings();
            _perLine.setText(Integer.toString(_initPerLine = cs.getChannelsPerLine()));
            _hGrouping.setText(Integer.toString(_initHGrouping = cs.getChannelGrouping()));
            _vGrouping.setText(Integer.toString(_initVGrouping = cs.getLineGrouping()));
        }
    }
