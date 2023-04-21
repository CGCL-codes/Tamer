    private void reading() throws IOException, ExperimentsFileFormatException {
        DataHandle dh = PluginDataHandle.getDataHandle();
        float percent = 0;
        float last_percent = 0;
        while (br.ready()) {
            all++;
            String line = br.readLine();
            ExperimentParserStruct interaction = RootExperimentsParser.readExperiment(line);
            String speciesName = interaction.getSpeciesName();
            String expNetworkName = IDCreator.createExpNetworkID(speciesName);
            PPINetworkExp netOrNull = dh.tryGetExpPPINetowrk(expNetworkName);
            if (netOrNull == null) {
                netOrNull = dh.createExpPPINetwork(speciesName, expNetworkName);
            }
            String edgeID = IDCreator.createExpInteractionID(interaction);
            dh.createInteractionExp(netOrNull, edgeID, interaction);
            created++;
            current = fis.getChannel().position();
            percent = current * 100 / (float) max;
            if (percent > last_percent + 1) {
                last_percent = percent;
                taskMonitor.setPercentCompleted(Math.round(percent));
            }
        }
    }
