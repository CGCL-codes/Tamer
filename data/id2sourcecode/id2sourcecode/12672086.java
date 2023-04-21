    private void addIndicator(final PanelBuilder builder, final CellConstraints cc, final int x, final int y, final int index) {
        Channel channel = getShow().getChannels().get(index);
        Level inputLevel = getShow().getChannelInputs().get(index);
        Level channelLevel = channel.getLevel();
        builder.add(new LevelDeltaIndicatorVertical(inputLevel, channelLevel), cc.xy(x, y));
        builder.add(new LevelLabel(inputLevel), cc.xy(x, y + 2));
        builder.add(new LevelLabel(channelLevel), cc.xy(x, y + 4));
        builder.add(createLabelChannelId(getShow(), index), cc.xy(x, y + 6));
    }
