        public void channelChanged(Channel channelSource) {
            if (source.parent.getChannel() != channelSource) {
                System.err.println("ERROR in TextureNodeConnection: got change event from unexpexted Channel.");
                return;
            }
            target.parent.getChannel().parameterChanged(null);
        }
