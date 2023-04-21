    public List<LabelBean> getChannels() {
        List<LabelBean> copy = new ArrayList<LabelBean>();
        for (LabelBean channel : channels) {
            copy.add(new LabelBean(channel.getName(), channel.getDesc(), channel.isDefaulted()));
        }
        return copy;
    }
