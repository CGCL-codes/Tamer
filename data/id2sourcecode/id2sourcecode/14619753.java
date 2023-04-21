    private void processSubscribe(AKSubscribe sub) {
        Logger.debug("Processing subscribe message : " + sub);
        List<Integer> requested = sub.getChannels();
        EntityID id = sub.getAgentID();
        Entity entity = world.getEntity(id);
        if (entity == null) {
            Logger.warn("Couldn't find entity " + id);
            return;
        }
        int max;
        if (entity instanceof FireBrigade || entity instanceof PoliceForce || entity instanceof AmbulanceTeam || entity instanceof Civilian) {
            max = platoonMax;
        } else if (entity instanceof FireStation || entity instanceof PoliceOffice || entity instanceof AmbulanceCentre) {
            max = centreMax;
        } else {
            Logger.warn("I don't know how to handle subscriptions for this entity: " + entity);
            return;
        }
        if (requested.size() > max) {
            Logger.warn("Agent " + id + " tried to subscribe to " + requested.size() + " channels but only " + max + " allowed");
            return;
        }
        for (Channel next : channels.values()) {
            next.removeSubscriber(entity);
        }
        for (int next : requested) {
            Channel channel = channels.get(next);
            if (channel == null) {
                Logger.warn("Agent " + id + " tried to subscribe to non-existant channel " + next);
            } else {
                channel.addSubscriber(entity);
            }
        }
    }
