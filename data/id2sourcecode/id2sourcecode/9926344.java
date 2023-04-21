    protected void scheduleNote(final PerformanceNode node, ToneSequence sequence, SimpleInstrument instrument) {
        try {
            if (node.getPitch() != null && node.getVelocity() > 0) {
                long elapsed = (System.nanoTime() - lastStarted);
                long delay = elapsed % sequence.getPerformanceLength();
                long elapsedLoopsMicro = (elapsed - delay) / 1000;
                long microPosition = microSecondsOffset + lastStartedMicro + elapsedLoopsMicro + node.getBegin() / 1000;
                final ShortMessage messageNoteOn = new ShortMessage();
                final ShortMessage messageNoteOff = new ShortMessage();
                if (instrument.instrument.toString().contains("Drumkit")) {
                    messageNoteOn.setMessage(ShortMessage.NOTE_ON, getChannel(instrument.instrument), instrument.drumkitInstrument, node.getVelocity());
                    messageNoteOff.setMessage(ShortMessage.NOTE_ON, getChannel(instrument.instrument), instrument.drumkitInstrument, 0);
                } else {
                    messageNoteOn.setMessage(ShortMessage.NOTE_ON, getChannel(instrument.instrument), node.getPitch(), node.getVelocity());
                    messageNoteOff.setMessage(ShortMessage.NOTE_OFF, getChannel(instrument.instrument), node.getPitch(), 0);
                }
                if (lastAdded == -1 && node.getBegin() == 0) {
                    receiver.send(messageNoteOn, microPosition);
                    receiver.send(messageNoteOff, microPosition + node.getDifference() / 1000);
                } else if (node.getBegin() < delay) {
                    delay = (node.getBegin() + sequence.getPerformanceLength() - delay) / 1000000;
                    microPosition += sequence.getPerformanceLength() / 1000;
                } else {
                    delay = (node.getBegin() - delay) / 1000000;
                }
                final long microPos = microPosition;
                timer.schedule(new TimerTask() {

                    public void run() {
                        receiver.send(messageNoteOn, microPos);
                        receiver.send(messageNoteOff, microPos + node.getDifference() / 1000);
                    }
                }, delay);
            }
        } catch (Exception exc) {
            System.err.println("error shedule note: " + exc.getMessage());
        }
    }
