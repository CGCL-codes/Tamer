    protected void exportVia(Interaction interaction, ExporterContext context) throws java.io.IOException {
        TextFormatter formatter = (TextFormatter) context.getFormatter();
        if (interaction.getChannel() != null) {
            formatter.record(" via " + interaction.getChannel().getName());
        }
    }
