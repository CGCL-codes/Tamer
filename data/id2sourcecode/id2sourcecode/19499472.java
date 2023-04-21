    public void actionPerformed(ActionEvent event) {
        ConsoleSettings cs = _console.getSettings();
        if (event.getSource().equals(_apply)) {
            int channels = Integer.parseInt(_numChannels.getText());
            int dimmers = Integer.parseInt(_numDimmers.getText());
            float upTime = Float.parseFloat(_upTime.getText());
            float downTime = Float.parseFloat(_downTime.getText());
            float gotoCueTime = Float.parseFloat(_gotoCueTime.getText());
            ACTSettingsShowGenralMod mod = new ACTSettingsShowGenralMod();
            if (_initRecordMode != _recordMode.getSelectedIndex()) {
                if (_rModes[_recordMode.getSelectedIndex()].equals("Tracking")) {
                    mod.setRecordMode(RecordMode.TRACKING);
                } else if (_rModes[_recordMode.getSelectedIndex()].equals("Cue Only")) {
                    mod.setRecordMode(RecordMode.CUE_ONLY);
                }
            }
            if (channels != _initChannels) mod.setChannels(channels);
            if (dimmers != _initDimmers) mod.setDimmers(dimmers);
            if (upTime != _initUpTime) mod.setUpTime((int) (upTime * 1000));
            if (downTime != _initDownTime) mod.setDownTime((int) (downTime * 1000));
            if (gotoCueTime != _initGotoCueTime) mod.setGotoCueTime((int) (gotoCueTime * 1000));
            if (!_title.getText().equals(_initTitle)) mod.setTitle(_title.getText());
            if (!_comment.getText().equals(_initComment)) mod.setComment(_comment.getText());
            _actInt.interprete(mod);
            cs = _console.getSettings();
            selectRecordMode(cs);
            _numChannels.setText(Integer.toString(_initChannels = cs.getChannels()));
            _numDimmers.setText(Integer.toString(_initDimmers = cs.getDimmers()));
            _upTime.setText(Float.toString(_initUpTime = (cs.getDefaultUpTime() / (float) 1000)));
            _downTime.setText(Float.toString(_initDownTime = (cs.getDefaultDownTime() / (float) 1000)));
            _gotoCueTime.setText(Float.toString(_initGotoCueTime = (cs.getDefaultGotoCueTime() / (float) 1000)));
            _title.setText(_initTitle = cs.getTitle());
            _comment.setText(_initComment = cs.getComment());
        }
    }
