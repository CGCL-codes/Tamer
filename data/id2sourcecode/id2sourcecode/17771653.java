    public void updateDisplay() {
        if (!isVisible()) return;
        if (!midiplayer.player_running) return;
        if (tabs.getSelectedComponent() == seqtab) {
            if (midiplayer.seq != seq) {
                seq = midiplayer.seq;
                while (seqmodel.getRowCount() != 0) seqmodel.removeRow(0);
                int row = 0;
                for (Track track : seq.getTracks()) {
                    int channel = 0;
                    int program = 0;
                    int bank_lsb = -1;
                    int bank_msb = -1;
                    String instext = "";
                    String tracktext = "";
                    int evcount = track.size();
                    for (int i = 0; i < evcount; i++) {
                        MidiEvent event = track.get(i);
                        if (event.getTick() != 0) break;
                        if (event.getMessage() instanceof MetaMessage) {
                            MetaMessage mmsg = (MetaMessage) event.getMessage();
                            try {
                                if (mmsg.getType() == 3) tracktext = new String(mmsg.getData(), "Latin1");
                                if (mmsg.getType() == 4) instext = new String(mmsg.getData(), "Latin1");
                            } catch (UnsupportedEncodingException e) {
                            }
                        }
                        if (event.getMessage() instanceof ShortMessage) {
                            ShortMessage smsg = (ShortMessage) event.getMessage();
                            channel = smsg.getChannel() + 1;
                            if (smsg.getCommand() == ShortMessage.PROGRAM_CHANGE) program = smsg.getData1();
                            if (smsg.getCommand() == ShortMessage.CONTROL_CHANGE) {
                                if (smsg.getData1() == 0) bank_msb = smsg.getData2();
                                if (smsg.getData1() == 32) bank_lsb = smsg.getData2();
                            }
                        }
                    }
                    String[] rowdata = new String[5];
                    if (instext.length() == 0) if (midiplayer.sbk != null) {
                        int bank = 0;
                        if (bank_msb != -1) bank += bank_msb * 128;
                        if (bank_lsb != -1) bank += bank_lsb;
                        Patch patch = new Patch(bank, program);
                        Instrument ins = midiplayer.sbk.getInstrument(patch);
                        if (ins != null) instext = ins.getName();
                    }
                    rowdata[0] = "" + row;
                    rowdata[1] = "" + channel;
                    rowdata[2] = "0," + program;
                    rowdata[3] = instext;
                    rowdata[4] = tracktext;
                    if (bank_msb != -1 || bank_lsb != -1) {
                        if (bank_msb == -1) bank_msb = 0;
                        if (bank_lsb == -1) bank_lsb = 0;
                        rowdata[2] = (bank_msb * 128 + bank_lsb) + "," + program;
                    }
                    seqmodel.addRow(rowdata);
                    row++;
                }
            }
        }
        if (tabs.getSelectedComponent() == sbktab) {
            if (midiplayer.sbk != sbk) if (midiplayer.sbk != null) {
                sbk = midiplayer.sbk;
                while (sbkmodel.getRowCount() != 0) sbkmodel.removeRow(0);
                sbkinfolab.setText("<html><body><table>" + "<tr><td><b>Name:</b></td><td>" + sbk.getName() + "</td>" + "<td><b>  Description:</b></td><td>" + sbk.getDescription() + "</td></tr>" + "<tr><td><b>Version:</b></td><td>" + sbk.getVersion() + "</td>" + "<td><b>  Vendor:</b></td><td>" + sbk.getVendor() + "</td></tr></table>");
                for (Instrument ins : sbk.getInstruments()) {
                    String[] rowdata = new String[3];
                    rowdata[0] = ins.getPatch().getBank() + "," + ins.getPatch().getProgram();
                    rowdata[1] = ins.getName();
                    rowdata[2] = ins.getClass().getSimpleName();
                    sbkmodel.addRow(rowdata);
                }
            }
        }
        if (tabs.getSelectedComponent() == chtab) {
            Soundbank sbk = midiplayer.sbk;
            MidiChannel[] channels = midiplayer.softsynth.getChannels();
            for (int i = 0; i < 16; i++) {
                MidiChannel channel = channels[i];
                if (sbk != null) {
                    Patch patch = new Patch(channel.getController(0) * 128 + channel.getController(32), channel.getProgram());
                    Instrument ins = sbk.getInstrument(patch);
                    if (ins != null) chmodel.setValueAt(channel.getProgram() + ": " + ins.getName(), i, 1);
                }
                chmodel.setValueAt(channel.getPitchBend() - 8192, i, 2);
                chmodel.setValueAt(channel.getController(7), i, 3);
                chmodel.setValueAt(channel.getController(10) - 64, i, 4);
                chmodel.setValueAt(channel.getController(91), i, 5);
                chmodel.setValueAt(channel.getController(93), i, 6);
            }
        }
        if (tabs.getSelectedComponent() == voctab) {
            {
                VoiceStatus[] voices = midiplayer.softsynth.getVoiceStatus();
                while (vocmodel.getRowCount() > voices.length) vocmodel.removeRow(vocmodel.getRowCount() - 1);
                while (vocmodel.getRowCount() < voices.length) vocmodel.addRow(new Object[] { false, 0, 0, 0, 0, 0 });
                for (int i = 0; i < voices.length; i++) {
                    VoiceStatus voc = voices[i];
                    vocmodel.setValueAt(voc.active, i, 0);
                    vocmodel.setValueAt(voc.channel + 1, i, 1);
                    vocmodel.setValueAt(voc.bank, i, 2);
                    vocmodel.setValueAt(voc.program, i, 3);
                    vocmodel.setValueAt(voc.note, i, 4);
                    vocmodel.setValueAt(voc.volume, i, 5);
                }
            }
        }
    }
