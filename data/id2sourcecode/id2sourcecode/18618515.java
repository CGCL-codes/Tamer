    public void loadUI() {
        if (loaded == false) {
            bundle = ResourceBundle.getBundle("javazoom/jlgui/player/amp/util/ui/visual");
            setBorder(new TitledBorder(getResource("title")));
            modePane = new JPanel();
            modePane.setBorder(new TitledBorder(getResource("mode.title")));
            modePane.setLayout(new FlowLayout());
            spectrumMode = new JRadioButton(getResource("mode.spectrum"));
            spectrumMode.addActionListener(this);
            oscilloMode = new JRadioButton(getResource("mode.oscilloscope"));
            oscilloMode.addActionListener(this);
            offMode = new JRadioButton(getResource("mode.off"));
            offMode.addActionListener(this);
            SpectrumTimeAnalyzer analyzer = null;
            if (player != null) {
                analyzer = player.getSkin().getAcAnalyzer();
                int displayMode = SpectrumTimeAnalyzer.DISPLAY_MODE_OFF;
                if (analyzer != null) {
                    displayMode = analyzer.getDisplayMode();
                }
                if (displayMode == SpectrumTimeAnalyzer.DISPLAY_MODE_SPECTRUM_ANALYSER) {
                    spectrumMode.setSelected(true);
                } else if (displayMode == SpectrumTimeAnalyzer.DISPLAY_MODE_SCOPE) {
                    oscilloMode.setSelected(true);
                } else if (displayMode == SpectrumTimeAnalyzer.DISPLAY_MODE_OFF) {
                    offMode.setSelected(true);
                }
            }
            ButtonGroup modeGroup = new ButtonGroup();
            modeGroup.add(spectrumMode);
            modeGroup.add(oscilloMode);
            modeGroup.add(offMode);
            modePane.add(spectrumMode);
            modePane.add(oscilloMode);
            modePane.add(offMode);
            spectrumPane = new JPanel();
            spectrumPane.setLayout(new BoxLayout(spectrumPane, BoxLayout.Y_AXIS));
            peaksBox = new JCheckBox(getResource("spectrum.peaks"));
            peaksBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            peaksBox.addActionListener(this);
            if ((analyzer != null) && (analyzer.isPeaksEnabled())) peaksBox.setSelected(true); else peaksBox.setSelected(false);
            spectrumPane.add(peaksBox);
            JLabel analyzerFalloffLabel = new JLabel(getResource("spectrum.analyzer.falloff"));
            analyzerFalloffLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            spectrumPane.add(analyzerFalloffLabel);
            int minDecay = (int) (SpectrumTimeAnalyzer.MIN_SPECTRUM_ANALYSER_DECAY * 100);
            int maxDecay = (int) (SpectrumTimeAnalyzer.MAX_SPECTRUM_ANALYSER_DECAY * 100);
            int decay = (maxDecay + minDecay) / 2;
            if (analyzer != null) {
                decay = (int) (analyzer.getSpectrumAnalyserDecay() * 100);
            }
            analyzerFalloff = new JSlider(JSlider.HORIZONTAL, minDecay, maxDecay, decay);
            analyzerFalloff.setMajorTickSpacing(1);
            analyzerFalloff.setPaintTicks(true);
            analyzerFalloff.setMaximumSize(new Dimension(150, analyzerFalloff.getPreferredSize().height));
            analyzerFalloff.setAlignmentX(Component.LEFT_ALIGNMENT);
            analyzerFalloff.setSnapToTicks(true);
            analyzerFalloff.addChangeListener(this);
            spectrumPane.add(analyzerFalloff);
            JLabel peaksFalloffLabel = new JLabel(getResource("spectrum.peaks.falloff"));
            peaksFalloffLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            spectrumPane.add(peaksFalloffLabel);
            int peakDelay = SpectrumTimeAnalyzer.DEFAULT_SPECTRUM_ANALYSER_PEAK_DELAY;
            int fps = SpectrumTimeAnalyzer.DEFAULT_FPS;
            if (analyzer != null) {
                fps = analyzer.getFps();
                peakDelay = analyzer.getPeakDelay();
            }
            peaksFalloff = new JSlider(JSlider.HORIZONTAL, 0, 4, computeSliderValue(peakDelay, fps));
            peaksFalloff.setMajorTickSpacing(1);
            peaksFalloff.setPaintTicks(true);
            peaksFalloff.setSnapToTicks(true);
            Hashtable labelTable = new Hashtable();
            labelTable.put(new Integer(0), new JLabel("Slow"));
            labelTable.put(new Integer(4), new JLabel("Fast"));
            peaksFalloff.setLabelTable(labelTable);
            peaksFalloff.setPaintLabels(true);
            peaksFalloff.setMaximumSize(new Dimension(150, peaksFalloff.getPreferredSize().height));
            peaksFalloff.setAlignmentX(Component.LEFT_ALIGNMENT);
            peaksFalloff.addChangeListener(this);
            spectrumPane.add(peaksFalloff);
            spectrumPane.setBorder(new TitledBorder(getResource("spectrum.title")));
            if (getResource("oscilloscope.title") != null) {
                oscilloPane = new JPanel();
                oscilloPane.setBorder(new TitledBorder(getResource("oscilloscope.title")));
            }
            GridBagLayout layout = new GridBagLayout();
            setLayout(layout);
            GridBagConstraints cnts = new GridBagConstraints();
            cnts.fill = GridBagConstraints.BOTH;
            cnts.gridwidth = 2;
            cnts.weightx = 2.0;
            cnts.weighty = 0.25;
            cnts.gridx = 0;
            cnts.gridy = 0;
            add(modePane, cnts);
            cnts.gridwidth = 1;
            cnts.weightx = 1.0;
            cnts.weighty = 1.0;
            cnts.gridx = 0;
            cnts.gridy = 1;
            add(spectrumPane, cnts);
            cnts.weightx = 1.0;
            cnts.weighty = 1.0;
            cnts.gridx = 1;
            cnts.gridy = 1;
            if (oscilloPane != null) add(oscilloPane, cnts);
            if (analyzer == null) {
                disablePane(modePane);
                disablePane(spectrumPane);
                disablePane(oscilloPane);
            }
            loaded = true;
        }
    }
