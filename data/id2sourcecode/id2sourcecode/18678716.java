    private void initComponents(CMSegmentGenerator cmsg) {
        channelGeneratorChooser = new ValuesGeneratorChooser(cmsg.getChannelGenerator());
        tabs.addTab(rb.getString("CMSegmentGenerator.channelGenerator"), generatorIcon, channelGeneratorChooser);
    }
