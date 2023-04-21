    public StripChannelBinding getChannelBinding(int slot) {
        try {
            StripChannelBinding p = channelBindings.get(slot);
            if (p == null) throw new IndexOutOfBoundsException();
            return p;
        } catch (IndexOutOfBoundsException e) {
            return new StripChannelBinding(slot);
        }
    }
