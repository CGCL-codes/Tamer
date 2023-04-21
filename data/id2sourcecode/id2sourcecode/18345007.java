    private void initContent() {
        tags = Arrays.asList(UMCLanguage.getText("MediacenterVdrPanel.tags").split(","));
        Channel[] channel = ConfigController.getInstance().getUMCConfig().getTv().getChannelArray();
        for (int i = 0; i < channel.length; i++) {
            tableVdrModel.addChannel(channel[i]);
        }
    }
