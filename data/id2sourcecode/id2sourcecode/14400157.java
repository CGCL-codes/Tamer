    public void mmChannelChanged(final MultiModeChannelChangedEvent ev) {
        MultiModeTableModel.this.fireTableRowsUpdated(ev.getChannel().intValue() - 1, ev.getChannel().intValue() - 1);
    }
