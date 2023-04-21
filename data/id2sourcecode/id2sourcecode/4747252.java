    public FormulaViewer(Simulation simulationToVisualize, GUI gui) {
        super("MRM - Formula Viewer", gui);
        currentSimulation = simulationToVisualize;
        currentRadioMedium = (MRM) currentSimulation.getRadioMedium();
        currentChannelModel = currentRadioMedium.getChannelModel();
        JPanel allComponents = new JPanel();
        allComponents.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        allComponents.setLayout(new BoxLayout(allComponents, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(allComponents);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setContentPane(scrollPane);
        JPanel collapsableArea;
        collapsableArea = createCollapsableArea("General parameters", allComponents);
        areaGeneral = collapsableArea;
        addBooleanParameter("apply_random", currentChannelModel.getParameterDescription("apply_random"), collapsableArea, currentChannelModel.getParameterBooleanValue("apply_random"));
        addDoubleParameter("snr_threshold", currentChannelModel.getParameterDescription("snr_threshold"), collapsableArea, currentChannelModel.getParameterDoubleValue("snr_threshold"));
        addDoubleParameter("bg_noise_mean", currentChannelModel.getParameterDescription("bg_noise_mean"), collapsableArea, currentChannelModel.getParameterDoubleValue("bg_noise_mean"));
        addDoubleParameter("bg_noise_var", currentChannelModel.getParameterDescription("bg_noise_var"), collapsableArea, currentChannelModel.getParameterDoubleValue("bg_noise_var"));
        addDoubleParameter("system_gain_mean", currentChannelModel.getParameterDescription("system_gain_mean"), collapsableArea, currentChannelModel.getParameterDoubleValue("system_gain_mean"));
        addDoubleParameter("system_gain_var", currentChannelModel.getParameterDescription("system_gain_var"), collapsableArea, currentChannelModel.getParameterDoubleValue("system_gain_var"));
        addDoubleParameter("wavelength", currentChannelModel.getParameterDescription("wavelength"), collapsableArea, currentChannelModel.getParameterDoubleValue("wavelength"));
        collapsableArea = createCollapsableArea("Transmitter parameters", allComponents);
        areaTransmitter = collapsableArea;
        addDoubleParameter("tx_power", currentChannelModel.getParameterDescription("tx_power"), collapsableArea, currentChannelModel.getParameterDoubleValue("tx_power"));
        addDoubleParameter("tx_antenna_gain", currentChannelModel.getParameterDescription("tx_antenna_gain"), collapsableArea, currentChannelModel.getParameterDoubleValue("tx_antenna_gain"));
        collapsableArea = createCollapsableArea("Receiver parameters", allComponents);
        areaReceiver = collapsableArea;
        addDoubleParameter("rx_sensitivity", currentChannelModel.getParameterDescription("rx_sensitivity"), collapsableArea, currentChannelModel.getParameterDoubleValue("rx_sensitivity"));
        addDoubleParameter("rx_antenna_gain", currentChannelModel.getParameterDescription("rx_antenna_gain"), collapsableArea, currentChannelModel.getParameterDoubleValue("rx_antenna_gain"));
        collapsableArea = createCollapsableArea("Ray Tracer parameters", allComponents);
        areaRayTracer = collapsableArea;
        addBooleanParameter("rt_disallow_direct_path", currentChannelModel.getParameterDescription("rt_disallow_direct_path"), collapsableArea, currentChannelModel.getParameterBooleanValue("rt_disallow_direct_path"));
        addBooleanParameter("rt_ignore_non_direct", currentChannelModel.getParameterDescription("rt_ignore_non_direct"), collapsableArea, currentChannelModel.getParameterBooleanValue("rt_ignore_non_direct"));
        addBooleanParameter("rt_fspl_on_total_length", currentChannelModel.getParameterDescription("rt_fspl_on_total_length"), collapsableArea, currentChannelModel.getParameterBooleanValue("rt_fspl_on_total_length"));
        addIntegerParameter("rt_max_rays", currentChannelModel.getParameterDescription("rt_max_rays"), collapsableArea, currentChannelModel.getParameterIntegerValue("rt_max_rays"));
        addIntegerParameter("rt_max_refractions", currentChannelModel.getParameterDescription("rt_max_refractions"), collapsableArea, currentChannelModel.getParameterIntegerValue("rt_max_refractions"));
        addIntegerParameter("rt_max_reflections", currentChannelModel.getParameterDescription("rt_max_reflections"), collapsableArea, currentChannelModel.getParameterIntegerValue("rt_max_reflections"));
        addIntegerParameter("rt_max_diffractions", currentChannelModel.getParameterDescription("rt_max_diffractions"), collapsableArea, currentChannelModel.getParameterIntegerValue("rt_max_diffractions"));
        addDoubleParameter("rt_refrac_coefficient", currentChannelModel.getParameterDescription("rt_refrac_coefficient"), collapsableArea, currentChannelModel.getParameterDoubleValue("rt_refrac_coefficient"));
        addDoubleParameter("rt_reflec_coefficient", currentChannelModel.getParameterDescription("rt_reflec_coefficient"), collapsableArea, currentChannelModel.getParameterDoubleValue("rt_reflec_coefficient"));
        addDoubleParameter("rt_diffr_coefficient", currentChannelModel.getParameterDescription("rt_diffr_coefficient"), collapsableArea, currentChannelModel.getParameterDoubleValue("rt_diffr_coefficient"));
        collapsableArea = createCollapsableArea("Shadowing parameters", allComponents);
        areaShadowing = collapsableArea;
        addDoubleParameter("obstacle_attenuation", currentChannelModel.getParameterDescription("obstacle_attenuation"), collapsableArea, currentChannelModel.getParameterDoubleValue("obstacle_attenuation"));
        currentChannelModel.addSettingsObserver(channelModelSettingsObserver);
        pack();
        setVisible(true);
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }
