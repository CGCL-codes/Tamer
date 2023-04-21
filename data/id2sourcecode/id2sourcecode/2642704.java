    public JComponent showResult(WorkResult results) throws RemoteException {
        JPanel pane = new JPanel();
        _results = results;
        pane.setLayout(new BorderLayout());
        pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        for (JobAttachment res : _results.getResults()) {
            sounds.add(res.fileURL());
        }
        pane.add(getPlayer());
        try {
            sequencer = MidiSystem.getSequencer();
            if (sequencer instanceof Synthesizer) {
                synthesizer = (Synthesizer) sequencer;
                channels = synthesizer.getChannels();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sequencer.addMetaEventListener(this);
        return pane;
    }
