    private void reading() throws IOException, InteractionsFileFormatException {
        DataHandle dh = PluginDataHandle.getDataHandle();
        float percent = 0;
        float last_percent = 0;
        while (br.ready()) {
            all++;
            InteractionParserStruct interaction = null;
            String line = br.readLine();
            interaction = RootInteractionsParser.readInteraction(line);
            String SourceID = interaction.getFrom();
            String TargetID = interaction.getTo();
            Double probability = interaction.getSim();
            Collection<SpeciesTreeNode> nets = dh.tryFindPPINetworkByProteinID(SourceID, TargetID);
            for (SpeciesTreeNode net : nets) {
                if (tresholds.containsKey(net.getID())) {
                    Double treshold = tresholds.get(net.getID());
                    if (treshold == null || probability > treshold) {
                        String EdgeID = IDCreator.createInteractionID(SourceID, TargetID, probability);
                        dh.createInteraction(EdgeID, SourceID, TargetID, probability);
                        created++;
                    }
                }
            }
            current = fis.getChannel().position();
            percent = current * 100 / (float) max;
            if (percent > last_percent + 1) {
                last_percent = percent;
                taskMonitor.setPercentCompleted(Math.round(percent));
            }
        }
    }
