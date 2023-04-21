    public void send(MidiDevice device, MidiMessage message, long lTimeStamp) {
        ShortMessage shortMessage;
        for (int i = 0; i < MonomeConfigurationFactory.getNumMonomeConfigurations(); i++) {
            MonomeConfiguration monomeConfig = MonomeConfigurationFactory.getMonomeConfiguration(i);
            if (monomeConfig != null) {
                monomeConfig.send(device, message, lTimeStamp);
            }
        }
        for (int i = 0; i < ArcConfigurationFactory.getNumArcConfigurations(); i++) {
            ArcConfiguration arcConfig = ArcConfigurationFactory.getArcConfiguration(i);
            if (arcConfig != null) {
                arcConfig.send(device, message, lTimeStamp);
            }
        }
        if (message instanceof ShortMessage) {
            shortMessage = (ShortMessage) message;
            switch(shortMessage.getCommand()) {
                case 0xF0:
                    if (shortMessage.getChannel() == 8) {
                        for (int i = 0; i < MonomeConfigurationFactory.getNumMonomeConfigurations(); i++) {
                            MonomeConfiguration monomeConfig = MonomeConfigurationFactory.getMonomeConfiguration(i);
                            if (monomeConfig != null) {
                                monomeConfig.tick(device);
                            }
                        }
                        for (int i = 0; i < ArcConfigurationFactory.getNumArcConfigurations(); i++) {
                            ArcConfiguration arcConfig = ArcConfigurationFactory.getArcConfiguration(i);
                            if (arcConfig != null) {
                                arcConfig.tick(device);
                            }
                        }
                    }
                    if (shortMessage.getChannel() == 0x0C) {
                        for (int i = 0; i < MonomeConfigurationFactory.getNumMonomeConfigurations(); i++) {
                            MonomeConfiguration monomeConfig = MonomeConfigurationFactory.getMonomeConfiguration(i);
                            if (monomeConfig != null) {
                                monomeConfig.reset(device);
                            }
                        }
                        for (int i = 0; i < ArcConfigurationFactory.getNumArcConfigurations(); i++) {
                            ArcConfiguration arcConfig = ArcConfigurationFactory.getArcConfiguration(i);
                            if (arcConfig != null) {
                                arcConfig.reset(device);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
