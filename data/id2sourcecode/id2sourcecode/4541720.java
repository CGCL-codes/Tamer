    public static Channel getMedChannel(Channel minChannel, Channel maxChannel) {
        int medIndex = 3 - (minChannel.index + maxChannel.index);
        return Channel.getChannel(medIndex);
    }
