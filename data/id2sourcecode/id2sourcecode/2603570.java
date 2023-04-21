    public DigitalInput_Simple() {
        super("Digital Input - 5 Eing�nge, davon 2 Counter");
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        for (int i = 0; i < 5; i++) {
            digitalInputViewLabels[i] = new SingleChannel(JWrapperK8055.DigitalInput.getChannelForIndex((short) (i + 1)), false);
        }
        ioListener = new IOListener() {

            @Override
            public IOChannels getDataType() {
                return IOChannels.DIGITAL;
            }

            @Override
            public Component getTargetComponent() {
                return DigitalInput_Simple.this;
            }

            @Override
            public boolean listenToAllChannels() {
                return true;
            }

            @Override
            public K8055Channel getChannel() {
                return null;
            }
        };
        for (int i = 0; i < 5; i++) {
            this.add(Box.createHorizontalStrut(3));
            this.add(digitalInputViewLabels[i]);
        }
        this.add(Box.createHorizontalGlue());
    }
