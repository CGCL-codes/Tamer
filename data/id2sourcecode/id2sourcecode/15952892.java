    private VisADInputSlider(DataReference ref, ScalarMap smap, float min, float max, float start, int sliderTicks, RealType rt, String n, boolean integralValues, boolean dynamicLabelWidth) throws VisADException, RemoteException {
        this.integralValues = integralValues;
        this.dynamicLabelWidth = dynamicLabelWidth;
        setAlignmentX(LEFT_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        sTicks = sliderTicks;
        Dimension d;
        slider = new JSlider(0, sTicks, sTicks / 2);
        d = slider.getMinimumSize();
        slider.setMinimumSize(new Dimension(SLIDER_WIDTH, d.height));
        d = slider.getPreferredSize();
        slider.setPreferredSize(new Dimension(SLIDER_WIDTH, d.height));
        d = slider.getMaximumSize();
        slider.setMaximumSize(new Dimension(SLIDER_WIDTH, d.height));
        autoScale = false;
        if (ref == null) {
            if (smap == null) {
                throw new VisADException("VisADInputSlider: must specify either a " + "DataReference or a ScalarMap!");
            }
            if (smap.getDisplayScalar() != Display.SelectValue) {
                throw new VisADException("VisADInputSlider: ScalarMap must be to " + "Display.SelectValue!");
            }
            if (!(smap.getScalar() instanceof RealType)) {
                throw new VisADException("VisADInputSlider: ScalarMap must be from " + "a RealType!");
            }
            map = smap;
            control = (ValueControl) smap.getControl();
            if (control == null) {
                throw new VisADException("VisADInputSlider: ScalarMap must be addMap'ed " + "to a Display");
            }
            sRef = null;
            sName = smap.getScalarName();
            start = (float) control.getValue();
            if (min == min && max == max && start == start) {
                sMinimum = min;
                sMaximum = max;
                if (integralValues) {
                    int tmp = (int) (sMaximum - sMinimum);
                    if (tmp != sTicks) {
                        sTicks = tmp;
                        slider.setMaximum(sTicks);
                    }
                }
                sCurrent = start;
                initLabel();
                smap.setRange(min, max);
                if (start < min || start > max) {
                    start = (min + max) / 2;
                    control.setValue(start);
                }
            } else {
                autoScale = true;
                initLabel();
            }
            control.addControlListener(this);
            smap.addScalarMapListener(this);
        } else {
            map = null;
            control = null;
            if (ref == null) {
                throw new VisADException("VisADInputSlider: DataReference " + "cannot be null!");
            }
            sRef = ref;
            Data data = ref.getData();
            if (data == null) {
                if (rt == null) {
                    throw new VisADException("VisADInputSlider: RealType cannot be null!");
                }
                if (n == null) {
                    throw new VisADException("VisADInputSlider: name cannot be null!");
                }
                realType = rt;
                if (min != min || max != max || start != start) {
                    throw new VisADException("VisADInputSlider: min, max, and start " + "cannot be NaN!");
                }
                sMinimum = min;
                sMaximum = max;
                sCurrent = (start < min || start > max) ? (min + max) / 2 : start;
                sRef.setData(new Real(realType, sCurrent));
            } else {
                if (!(data instanceof Real)) {
                    throw new VisADException("VisADInputSlider: DataReference " + "must point to a Real!");
                }
                Real real = (Real) data;
                realType = (RealType) real.getType();
                sCurrent = (float) real.getValue();
                if (min != min || max != max) {
                    throw new VisADException("VisADInputSlider: minimum and maximum " + "cannot be NaN!");
                }
                sMinimum = min;
                sMaximum = max;
                if (sCurrent < min || sCurrent > max) sCurrent = (min + max) / 2;
            }
            sName = (n != null) ? n : realType.getName();
            initLabel();
            CellImpl cell = new CellImpl() {

                public void doAction() throws VisADException, RemoteException {
                    if (sRef != null) {
                        double val;
                        try {
                            val = ((Real) sRef.getData()).getValue();
                            if (!Util.isApproximatelyEqual(sCurrent, val)) updateSlider(val);
                        } catch (RemoteException re) {
                            if (visad.collab.CollabUtil.isDisconnectException(re)) {
                                sRef = null;
                            }
                            throw re;
                        }
                    }
                }
            };
            if (ref instanceof RemoteDataReference) {
                RemoteCell remoteCell = new RemoteCellImpl(cell);
                remoteCell.addReference(ref);
            } else cell.addReference(ref);
        }
        add(slider);
        add(label);
        add(inputValue);
        slider.addChangeListener(this);
        updateSlider(start);
    }
