    private void patch(final PatchParameters parameters, final boolean update, final Listener listener) {
        if (update) {
            for (int i = 0; i < parameters.size(); i++) {
                int dimmerId = parameters.getDimmerId(i);
                int channelId = parameters.getChannelId(i);
                if (channelId == -1) {
                    if (isPrePatch()) {
                        channelId = dimmerId + PRE_PATCH_START;
                    }
                }
                Dimmer dimmer = context.getShow().getDimmers().get(dimmerId);
                dimmer.setLanboxChannelId(channelId);
            }
            PatchParameters[] pp = parameters.split();
            for (int i = 0; i < pp.length; i++) {
                Command command = new CommonSetPatch(pp[i]);
                if (i == pp.length - 1 && listener != null) {
                    command.add(new CommandListener() {

                        public void commandPerformed(final Command c) {
                            if (listener != null) {
                                listener.changed();
                            }
                        }
                    });
                }
                context.getLanbox().execute(command);
            }
        } else {
            if (listener != null) {
                listener.changed();
            }
        }
    }
