    public float[] getChannelSource(Channel channel) {
        ArrayList<Float> results = new ArrayList<Float>();
        IValueSet[] sets = channelSets.getPriorityOrderSets(channel.address);
        float add = channel.address;
        for (IValueSet s : sets) {
            Channel c = s.getRawChannel(channel.address);
            if (s.getRawChannel(channel.address) != null) results.add((float) s.getSource());
        }
        float[] r = new float[results.size()];
        for (int i = 0; i < results.size(); i++) r[i] = results.get(i);
        return r;
    }
