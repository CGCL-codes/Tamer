    public void mmChannelChanged(MultiModeChannelChangedEvent ev) {
        if (mmch != null) if (ev.getChannel().equals(mmch.getChannel())) try {
            setSelectedIndex(IntPool.get(mmch.getPreset().intValue() + 1));
        } catch (ParameterException e) {
            e.printStackTrace();
        }
    }
