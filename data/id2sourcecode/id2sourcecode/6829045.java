    private Channel<?> getChannel(Object channelDesc) {
        if (channelDesc instanceof List) {
            HGHandle jobId = getPart(channelDesc, 0);
            String channelId = getPart(channelDesc, 1);
            Channel<?> logicalChannel = network.getChannel(channelId);
            JobDataFlow<?> jobNetwork = (JobDataFlow<?>) network;
            return jobNetwork.getChannelManager().getJobChannel(jobNetwork, logicalChannel, (Job) getThisPeer().getGraph().get(jobId));
        } else return network.getChannel(channelDesc.toString());
    }
