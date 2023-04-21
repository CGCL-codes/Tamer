    @Override
    public String getChannelTitle(int channel) {
        PropertyDefinition pd = this.getPropertyDefinitionForLogicalChannel(channel);
        if (pd == null) return ""; else return pd.getTitle();
    }
