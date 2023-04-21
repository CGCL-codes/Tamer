    @Override
    public AmptReceiver getAmptReceiver() {
        return (new AmptReceiver() {

            @Override
            public void filter(MidiMessage message, long timeStamp) {
                if (message instanceof ShortMessage) {
                    ShortMessage sMsg = (ShortMessage) message;
                    int command = sMsg.getCommand();
                    int channel = sMsg.getChannel();
                    int tone = sMsg.getData1();
                    int velocity = sMsg.getData2();
                    String key = channel + ":" + tone;
                    if (command == ShortMessage.NOTE_OFF || (command == ShortMessage.NOTE_ON && velocity == 0)) {
                        synchronized (ArpeggiatorFilterDevice.this) {
                            _arpeggios.put(key, false);
                        }
                    } else {
                        if (command == ShortMessage.NOTE_ON) {
                            float noteFactor;
                            int[] intervals;
                            ArpeggioTask arpTask;
                            synchronized (ArpeggiatorFilterDevice.this) {
                                if (!_arpeggios.containsKey(key)) {
                                    _arpeggios.put(key, true);
                                    noteFactor = 60000 / _noteValue.getNotesPerBeat();
                                    intervals = _arp.getIntervals();
                                    arpTask = new ArpeggioTask(key, intervals, channel, velocity, tone, noteFactor, _motion == Arpeggio.RANDOM);
                                    execute(arpTask);
                                } else if (!_arpeggios.get(key)) {
                                    _arpeggios.put(key, true);
                                    noteFactor = 60000 / _noteValue.getNotesPerBeat();
                                    intervals = _arp.getIntervals();
                                    arpTask = new ArpeggioTask(key, intervals, channel, velocity, tone, noteFactor, _motion == Arpeggio.RANDOM);
                                    execute(arpTask);
                                }
                            }
                        }
                    }
                } else sendNow(message);
            }
        });
    }
