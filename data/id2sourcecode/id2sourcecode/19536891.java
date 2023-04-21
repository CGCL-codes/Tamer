            public void actionPerformed(ActionEvent e) {
                Instrument instr = (Instrument) instrumentBox.getSelectedItem();
                synth.loadInstrument(instr);
                Patch patch = instr.getPatch();
                synth.getChannels()[4].programChange(patch.getBank(), patch.getProgram());
            }
