        @Override
        public void run() {
            try {
                parser.parseDocument();
                channelInfo = parser.getChannel();
                downloadQueue.clear();
                for (Item item : parser.getItems()) {
                    if (items.containsKey(item.getIdentifier()) == false) {
                        downloadQueue.add(item);
                    }
                }
                if (downloadQueue.isEmpty() == false) {
                    for (INewElementListener newElementListener : newElementListeners) {
                        newElementListener.newElementFound(podcast);
                    }
                }
            } catch (IOException ex) {
                for (INetworkErrorListener networkErrorListener : networkErrorListeners) {
                    networkErrorListener.networkErrorDetected(ex);
                }
            }
        }
