    private JPanel getJContentPane() {
        if (jContentPane == null) {
            midiCCValLbl = new JLabel();
            midiCCValLbl.setBounds(new Rectangle(15, 180, 116, 21));
            midiCCValLbl.setText("MIDI CC Value");
            midiCCLbl = new JLabel();
            midiCCLbl.setBounds(new Rectangle(15, 155, 116, 21));
            midiCCLbl.setText("MIDI CC #");
            pageChangeTimerLBL = new JLabel();
            pageChangeTimerLBL.setBounds(new Rectangle(15, 205, 116, 21));
            pageChangeTimerLBL.setText("PC Button Delay");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getPageLabel(), null);
            jContentPane.add(getPageCB(), null);
            jContentPane.add(getChannelLBL(), null);
            jContentPane.add(getChannelTF(), null);
            jContentPane.add(getNoteLBL(), null);
            jContentPane.add(getNoteTF(), null);
            jContentPane.add(getMonomeChangeCB(), null);
            jContentPane.add(getMidiChangeCB(), null);
            jContentPane.add(getMonomeChangeLBL(), null);
            jContentPane.add(getMidiChangingLBL(), null);
            jContentPane.add(getSaveBtn(), null);
            jContentPane.add(getCancelBtn(), null);
            jContentPane.add(pageChangeTimerLBL, null);
            jContentPane.add(getPageChangeDelayTF(), null);
            jContentPane.add(midiCCLbl, null);
            jContentPane.add(getMidiCCTF(), null);
            jContentPane.add(midiCCValLbl, null);
            jContentPane.add(getMidiCCValTF(), null);
            if (arc == null && monome != null) {
                jContentPane.add(getLinkedDeviceCB(), null);
                jContentPane.add(getLinkedPageCB(), null);
            }
        }
        return jContentPane;
    }
