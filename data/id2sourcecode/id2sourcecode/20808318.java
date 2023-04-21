    private void reading() throws IOException, InteractionsFileFormatException {
        DataHandle dh = PluginDataHandle.getDataHandle();
        int count = 0;
        float percent = 0;
        float last_percent = 0;
        long current;
        while (br.ready()) {
            lineNumber++;
            InteractionParserStruct interaction = null;
            String line = br.readLine();
            interaction = RootInteractionsParser.readInteraction(line);
            String SourceID = interaction.getFrom();
            String TargetID = interaction.getTo();
            Double probability = interaction.getSim();
            if (treshold == null || probability > treshold) {
                if (network.containsProtein(TargetID) && network.containsProtein(SourceID)) {
                    String EdgeID = IDCreator.createInteractionID(SourceID, TargetID, probability);
                    dh.createInteraction(EdgeID, SourceID, TargetID, probability, network);
                    count++;
                }
            }
            current = fis.getChannel().position();
            percent = current * 100 / (float) max;
            if (percent > last_percent + 1) {
                last_percent = percent;
                taskMonitor.setPercentCompleted(Math.round(percent));
                taskMonitor.setStatus("Interactions are loading for: " + network.getID() + "  " + count);
            }
        }
    }
