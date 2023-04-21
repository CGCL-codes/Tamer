    private void sortOnChannelName() {
        Collections.sort(patchDetails, new Comparator<PatchDetail>() {

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
                    String name1 = dimmer1.getChannel().getName();
                    String name2 = dimmer2.getChannel().getName();
                    result = name1.compareTo(name2);
                }
                if (result == 0) {
                    result = dimmer1.getId() - dimmer2.getId();
                }
                return result;
            }
        });
    }
