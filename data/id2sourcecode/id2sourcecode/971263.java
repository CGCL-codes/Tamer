    void messageReceived(MidiMessage message, int device) {
        if (Util.getDebugLevel() > 64) {
            ShortMessage sm = null;
            if (message instanceof ShortMessage) {
                sm = (ShortMessage) message;
            }
            if (sm == null || sm.getCommand() != 240 || Util.getDebugLevel() > 156) {
                Util.debug("***************************************");
                Util.debug("Byte Length: " + message.getLength());
                Util.debug("Status: " + message.getStatus());
                Util.debug("---------------------------------------");
                if (sm != null) {
                    Util.debug("Channel: " + sm.getChannel());
                    Util.debug("Command: " + sm.getCommand());
                    Util.debug("Data1: " + sm.getData1());
                    Util.debug("Data2: " + sm.getData2());
                }
                Util.debug("***************************************");
            }
        }
        if (message instanceof ShortMessage) {
            final ShortMessage shortMessage = (ShortMessage) message;
            switch(shortMessage.getCommand()) {
                case ShortMessage.CONTROL_CHANGE:
                    if (waitingForController) {
                        waitingForController = false;
                        DialogSettings.setController((byte) shortMessage.getData1());
                    } else {
                        if (waitingForNote) {
                            DialogSettings.setNote((byte) -1);
                        }
                        if (shortMessage.getData1() == pause) {
                            if (pauseAllowed) {
                                Game.togglePause();
                                pauseAllowed = false;
                                (new Thread() {

                                    public void run() {
                                        final int pauseId = ++pauseAllowedId;
                                        try {
                                            Thread.sleep(PAUSE_DELAY);
                                        } catch (InterruptedException e) {
                                            if (Util.getDebugLevel() > 90) e.printStackTrace();
                                        }
                                        if (pauseId == pauseAllowedId) pauseAllowed = true;
                                    }
                                }).start();
                            }
                        }
                    }
                    break;
                case ShortMessage.NOTE_ON:
                    if (waitingForNote) {
                        if (shortMessage.getData2() != 0) {
                            waitingForNote = false;
                            DialogSettings.setNote((byte) shortMessage.getData1());
                        }
                    } else if (waitingForController) {
                        DialogSettings.setController((byte) -1);
                    }
                    if (shortMessage.getChannel() != 9) checkMessage(shortMessage, device, true);
                    break;
                case ShortMessage.NOTE_OFF:
                    if (shortMessage.getChannel() != 9) checkMessage(shortMessage, device, false);
                    break;
                case ShortMessage.POLY_PRESSURE:
                    if (shortMessage.getChannel() != 9) {
                        alterMessage(shortMessage, device, true);
                        Game.activateKey(shortMessage, device);
                    }
                    break;
                case ShortMessage.CHANNEL_PRESSURE:
                    if (shortMessage.getChannel() != 9) {
                        alterMessage(shortMessage, device, false);
                        Game.reactivateKeys(device, shortMessage.getChannel(), shortMessage.getData1());
                    }
                    break;
            }
        }
    }
