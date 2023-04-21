    public void export(ModelObject modelObject, ExporterContext context) throws java.io.IOException {
        ChannelList chlist = (ChannelList) modelObject;
        TextFormatter formatter = (TextFormatter) context.getFormatter();
        formatter.record("channel\t");
        for (int i = 0; i < chlist.getChannels().size(); i++) {
            if (i > 0) {
                formatter.record(", ");
            }
            context.export(chlist.getChannels().get(i));
        }
    }
