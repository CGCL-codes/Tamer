        public void run() {
            this.running = true;
            AuditTrailEntryList ateList = log.getInstance(index).getAuditTrailEntryList();
            EventClassTable ecTable = clusters.getOrderedEventClassTable();
            double factor = 97.0 / (double) ecTable.size();
            int foreLastNote = -1;
            int lastNote = -1;
            try {
                Receiver synthRcvr = synth.getReceiver();
                for (int i = 0; i < ateList.size(); i++) {
                    if (running == false) {
                        break;
                    }
                    views.get(index).setActiveIndex(i);
                    AuditTrailEntry ate = ateList.get(i);
                    int ecIndex = ecTable.getIndex(ate.getElement());
                    int note = 112 - (int) (ecIndex * factor);
                    if (foreLastNote >= 0) {
                        ShortMessage myMsg = new ShortMessage();
                        myMsg.setMessage(ShortMessage.NOTE_OFF, 4, foreLastNote, 110);
                        synthRcvr.send(myMsg, -1);
                    }
                    ShortMessage myMsg = new ShortMessage();
                    myMsg.setMessage(ShortMessage.NOTE_ON, 4, note, 110);
                    synthRcvr.send(myMsg, -1);
                    foreLastNote = lastNote;
                    lastNote = note;
                    try {
                        Thread.sleep(220);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (MidiChannel channel : synth.getChannels()) {
                    channel.allNotesOff();
                }
                for (StreamView view : views) {
                    view.setActiveIndex(-1);
                }
                playButton.setText("play");
                playThread = null;
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
