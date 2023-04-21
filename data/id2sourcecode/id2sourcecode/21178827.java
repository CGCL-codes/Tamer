    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        Synthesizer synth = (Synthesizer) this.midiDevice;
        Instrument instrument = namedInstrumentMap.get((String) tableModel.getValueAt(row, e.getColumn()));
        int channelNum = (Integer) tableModel.getValueAt(row, 0) - 1;
        int bank = instrument.getPatch().getBank();
        int program = instrument.getPatch().getProgram();
        synth.getChannels()[channelNum].programChange(bank, program);
    }
