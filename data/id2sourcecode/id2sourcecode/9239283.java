        public static Channel getBesideSecondaryChannel(Channel secondaryColorChannel) {
            if (!secondaryColorChannel.isSecondaryColorChannel()) {
                throw new IllegalArgumentException("is not secondaryColorChannel");
            }
            int besideIndex = secondaryColorChannel.index - 3;
            return Channel.getChannel(besideIndex);
        }
