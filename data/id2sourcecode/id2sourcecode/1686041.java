    public void savePatch(final Listener listener) {
        PatchParameters parameters = new PatchParameters();
        Dimmers dimmers = context.getShow().getDimmers();
        for (int i = 0; i < dimmers.size(); i++) {
            Dimmer dimmer = dimmers.get(i);
            parameters.add(i, dimmer.getChannelId());
        }
        patch(parameters, true, listener);
    }
