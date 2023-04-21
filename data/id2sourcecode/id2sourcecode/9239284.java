        public static Channel getBesidePrimaryChannel(Channel primaryColorChannel1, Channel primaryColorChannel2) {
            if (!primaryColorChannel1.isPrimaryColorChannel() || !primaryColorChannel2.isPrimaryColorChannel()) {
                throw new IllegalArgumentException("is not primaryColorChannel1");
            }
            int besideIndex = 6 - (primaryColorChannel1.index + primaryColorChannel2.index);
            return Channel.getChannel(besideIndex);
        }
