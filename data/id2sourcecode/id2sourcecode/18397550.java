    private void note(int nCommand, int nKeyCode) {
        if (nKeyCode <= m_anManualFromKeyCode.length) {
            int nManual = m_anManualFromKeyCode[nKeyCode];
            if (nManual != -1) {
                ShortMessage sm = new ShortMessage();
                try {
                    sm.setMessage(nCommand, getChannel(nManual), getNote(nManual, nKeyCode), getVelocity(nManual));
                } catch (InvalidMidiDataException e) {
                    Debug.out(e);
                }
                getReceiver(nManual).send(sm, -1);
            } else {
                Debug.out("Unrecognized keyCode " + nKeyCode);
            }
        } else {
            Debug.out("Unrecognized keyCode " + nKeyCode);
        }
    }
