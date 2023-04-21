    public JComponent showResult(WorkResult results) throws RemoteException {
        JPanel pane = new JPanel();
        _results = results;
        pane.setLayout(new BorderLayout());
        pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        for (Enumeration e = _results.getResults().elements(); e.hasMoreElements(); ) {
            JobResult res = (JobResult) e.nextElement();
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
