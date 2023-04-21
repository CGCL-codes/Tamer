    public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode rootNode = ChannelEditor.application.getChannelListingPanel().getRootNode();
        File loadedFile = ChannelEditor.application.getChannelFile();
        String loadedFilename = "";
        String loadedFilepath = "";
        long loadedFilesize = 0;
        if (loadedFile != null) {
            loadedFilename = loadedFile.getName();
            loadedFilepath = loadedFile.getParent();
            loadedFilesize = loadedFile.length();
        }
        int countCategories = 0;
        int countChannels = 0;
        int countTV = 0;
        int countRadio = 0;
        int countService = 0;
        long newFilesize = 0;
        Enumeration enumer = rootNode.preorderEnumeration();
        while (enumer.hasMoreElements()) {
            DefaultMutableTreeNode mutNode = (DefaultMutableTreeNode) enumer.nextElement();
            ChannelElement channelElement = (ChannelElement) mutNode.getUserObject();
            if (channelElement.isCategory()) {
                countCategories++;
            } else {
                if (!mutNode.isRoot()) {
                    countChannels++;
                }
                if (channelElement.isTelevision()) {
                    countTV++;
                } else if (channelElement.isRadio()) {
                    countRadio++;
                } else if (channelElement.isService()) {
                    countService++;
                }
            }
            newFilesize += channelElement.outputString().length();
        }
        String text = Messages.getString("StatisticAction.4") + Messages.getString("StatisticAction.5") + Messages.getString("StatisticAction.6") + Messages.getString("StatisticAction.7") + Messages.getString("StatisticAction.8") + loadedFilename + Messages.getString("StatisticAction.9") + Messages.getString("StatisticAction.10") + loadedFilepath + Messages.getString("StatisticAction.11") + Messages.getString("StatisticAction.12") + loadedFilesize + Messages.getString("StatisticAction.13") + Messages.getString("StatisticAction.14") + Messages.getString("StatisticAction.15") + Messages.getString("StatisticAction.16") + Messages.getString("StatisticAction.17") + countChannels + Messages.getString("StatisticAction.18") + Messages.getString("StatisticAction.19") + countCategories + Messages.getString("StatisticAction.20") + Messages.getString("StatisticAction.21") + countTV + Messages.getString("StatisticAction.22") + Messages.getString("StatisticAction.23") + countRadio + Messages.getString("StatisticAction.24") + Messages.getString("StatisticAction.25") + countService + Messages.getString("StatisticAction.26") + Messages.getString("StatisticAction.27") + newFilesize + Messages.getString("StatisticAction.28") + Messages.getString("StatisticAction.29") + Messages.getString("StatisticAction.30");
        JOptionPane.showMessageDialog(ChannelEditor.application, text, Messages.getString("StatisticAction.31"), JOptionPane.PLAIN_MESSAGE);
    }
