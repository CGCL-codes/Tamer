    public void executeMsg(Environmental myHost, CMMsg msg) {
        switch(moodCode) {
            case 7:
                {
                    if ((msg.sourceMinor() == CMMsg.TYP_DEATH) && (msg.tool() == affected) && (msg.source() != null) && (this.lastOne != msg.source())) {
                        lastOne = msg.source();
                        int channelIndex = -1;
                        int channelC = -1;
                        String[] CHANNELS = CMLib.channels().getChannelNames();
                        for (int c = 0; c < CHANNELS.length; c++) if (CMStrings.contains(BOAST_CHANNELS, CHANNELS[c])) {
                            channelIndex = CMLib.channels().getChannelIndex(CHANNELS[c]);
                            channelC = c;
                            if (channelIndex >= 0) break;
                        }
                        if (channelIndex >= 0) {
                            String addOn = ".";
                            switch(CMLib.dice().roll(1, 10, 0)) {
                                case 1:
                                    addOn = ", but that`s not suprising, is it?";
                                    break;
                                case 2:
                                    addOn = ". I rock.";
                                    break;
                                case 3:
                                    addOn = ". I am **POWERFUL**.";
                                    break;
                                case 4:
                                    addOn = ". I am sooo cool.";
                                    break;
                                case 5:
                                    addOn = ". You can`t touch me.";
                                    break;
                                case 6:
                                    addOn = ".. never had a chance, either.";
                                    break;
                                case 7:
                                    addOn = ", with my PINKEE!";
                                    break;
                                default:
                                    break;
                            }
                            ((MOB) affected).doCommand(CMParms.makeVector(CHANNELS[channelC], "*I* just killed " + msg.source().Name() + addOn), Command.METAFLAG_FORCED);
                        }
                    }
                    break;
                }
            default:
                break;
        }
        super.executeMsg(myHost, msg);
    }
