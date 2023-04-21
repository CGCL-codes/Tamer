            public int compare(NodeChannel b1, NodeChannel b2) {
                boolean isError1 = errorChannels.containsKey(b1.getChannelId());
                boolean isError2 = errorChannels.containsKey(b2.getChannelId());
                if (!isError1 && !isError2) {
                    return b1.getProcessingOrder() < b2.getProcessingOrder() ? -1 : 1;
                } else if (isError1 && isError2) {
                    return errorChannels.get(b1.getChannelId()).compareTo(errorChannels.get(b2.getChannelId()));
                } else if (!isError1 && isError2) {
                    return -1;
                } else {
                    return 1;
                }
            }
