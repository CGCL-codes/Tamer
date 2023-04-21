    public CounterInput_Simple() {
        super("Counter - 2 Eing�nge");
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Box top = new Box(BoxLayout.X_AXIS), bottom = new Box(BoxLayout.X_AXIS);
        value1 = new JCopyableLabel("CounterInput 1: 0", 15);
        value2 = new JCopyableLabel("CounterInput 2: 0", 15);
        top.add(Box.createHorizontalStrut(3));
        top.add(value1);
        top.add(Box.createHorizontalStrut(3));
        top.add(getCounterResetButton(1));
        top.add(Box.createHorizontalStrut(24));
        top.add(new JCopyableLabel("Entprellzeit :"));
        addDebounceTimeSlider(1, top);
        top.add(Box.createHorizontalGlue());
        bottom.add(Box.createHorizontalStrut(3));
        bottom.add(value2);
        bottom.add(Box.createHorizontalStrut(3));
        bottom.add(getCounterResetButton(2));
        bottom.add(Box.createHorizontalStrut(24));
        bottom.add(new JCopyableLabel("Entprellzeit :"));
        addDebounceTimeSlider(2, bottom);
        bottom.add(Box.createHorizontalGlue());
        this.add(top);
        this.add(bottom);
        ioListener = new IOListener() {

            @Override
            public boolean listenToAllChannels() {
                return true;
            }

            @Override
            public Component getTargetComponent() {
                return CounterInput_Simple.this;
            }

            @Override
            public IOChannels getDataType() {
                return IOChannels.COUNTER;
            }

            @Override
            public K8055Channel getChannel() {
                return null;
            }
        };
    }
