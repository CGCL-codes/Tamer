    @Override
    public void process(int time, Collection<? extends Command> agentCommands) {
        Logger.debug("ChannelCommunicationModel processing commands at time " + time + ": " + agentCommands);
        super.process(time, agentCommands);
        for (Channel next : channels.values()) {
            next.timestep();
        }
        for (Command next : agentCommands) {
            if (next instanceof AKSubscribe) {
                processSubscribe((AKSubscribe) next);
            }
        }
        for (Command next : agentCommands) {
            if (next instanceof AKSpeak) {
                try {
                    AKSpeak speak = (AKSpeak) next;
                    int channelNumber = speak.getChannel();
                    Channel channel = channels.get(channelNumber);
                    Logger.debug("Processing speak: " + speak);
                    if (channel == null) {
                        throw new InvalidMessageException("Unrecognised channel: " + channelNumber);
                    } else {
                        channel.push(speak);
                    }
                } catch (InvalidMessageException e) {
                    Logger.warn("Invalid message: " + next + ": " + e.getMessage());
                }
            }
        }
        for (Entity agent : world.getEntitiesOfType(StandardEntityURN.FIRE_BRIGADE, StandardEntityURN.FIRE_STATION, StandardEntityURN.POLICE_FORCE, StandardEntityURN.POLICE_OFFICE, StandardEntityURN.AMBULANCE_TEAM, StandardEntityURN.AMBULANCE_CENTRE, StandardEntityURN.CIVILIAN)) {
            for (Channel channel : channels.values()) {
                addHearing(agent, channel.getMessagesForAgent(agent));
            }
        }
    }
