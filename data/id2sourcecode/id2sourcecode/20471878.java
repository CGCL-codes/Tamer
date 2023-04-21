    private String string() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < patchDetails.size(); i++) {
            PatchDetail detail = patchDetails.get(i);
            Dimmer dimmer = detail.getDimmer();
            Channel channel = dimmer.getChannel();
            b.append(dimmer.getId() + 1);
            b.append('\t');
            b.append(dimmer.getName());
            b.append('\t');
            if (channel != null) {
                b.append(channel.getId() + 1);
            }
            b.append('\t');
            if (channel != null) {
                b.append(channel.getName());
            }
            b.append('\n');
        }
        return b.toString();
    }
