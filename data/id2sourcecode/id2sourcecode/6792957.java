            public int compare(final PatchDetail pd1, final PatchDetail pd2) {
                Dimmer dimmer1 = pd1.getDimmer();
                Dimmer dimmer2 = pd2.getDimmer();
                int result = 0;
                if (!dimmer1.isPatched()) {
                    if (dimmer2.isPatched()) {
                        result = 1;
                    }
                } else if (!dimmer2.isPatched()) {
                    result = -1;
                } else {
                    result = dimmer1.getChannelId() - dimmer2.getChannelId();
                }
                if (result == 0) {
                    result = dimmer1.getId() - dimmer2.getId();
                }
                return result;
            }
