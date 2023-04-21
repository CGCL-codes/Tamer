    public AnalogInput_Simple() {
        super("Analog Input - 2 Eing�nge");
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        view1 = new JProgressBar(SwingConstants.HORIZONTAL, AnalogInput.MIN_VALUE, AnalogInput.MAX_VALUE);
        view2 = new JProgressBar(SwingConstants.HORIZONTAL, AnalogInput.MIN_VALUE, AnalogInput.MAX_VALUE);
        valueLabel1 = new JCopyableLabel(AnalogInput.toRepresentingString(AnalogInput.MIN_VALUE), 15);
        valueLabel2 = new JCopyableLabel(AnalogInput.toRepresentingString(AnalogInput.MIN_VALUE), 15);
        this.add(Box.createHorizontalStrut(3));
        this.add(new JCopyableLabel("A In 1: "));
        this.add(view1);
        this.add(Box.createHorizontalStrut(3));
        this.add(valueLabel1);
        this.add(Box.createHorizontalStrut(8));
        {
            JSeparator j = new JSeparator(SwingConstants.VERTICAL);
            j.setMaximumSize(new Dimension(4, Integer.MAX_VALUE));
            j.setMinimumSize(new Dimension(4, Integer.MIN_VALUE));
            this.add(j);
        }
        this.add(Box.createHorizontalStrut(8));
        this.add(new JCopyableLabel("A In 2: "));
        this.add(view2);
        this.add(Box.createHorizontalStrut(3));
        this.add(valueLabel2);
        this.add(Box.createHorizontalGlue());
        ioListener = new IOListener() {

            @Override
            public IOChannels getDataType() {
                return IOChannels.ANALOG;
            }

            @Override
            public Component getTargetComponent() {
                return AnalogInput_Simple.this;
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
    }
