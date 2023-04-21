    public AdvancedAudioPropertiesDialog(JFrame o, AudioDriver ad, GUI gui) {
        owner = o;
        this.ad = ad;
        this.gui = gui;
        dialog = new JDialog(owner, "Advanced Audio Properties", true);
        dialog.setResizable(false);
        if (ad != null) {
            outputDevices = new JComboBox();
            ad.stop();
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            for (int i = 0; i < mixers.length; i++) {
                Mixer mixer = AudioSystem.getMixer(mixers[i]);
                Line.Info[] info = mixer.getSourceLineInfo();
                boolean b = false;
                for (int j = 0; j < info.length; ++j) {
                    if (info[j].toString().indexOf("buffer") >= 0) b = true;
                }
                if (b) {
                    this.mixers.add(mixers[i]);
                    outputDevices.addItem(mixers[i].getName());
                }
            }
            ad.start();
            for (int i = 0; i < outputDevices.getItemCount(); ++i) {
                if (((Mixer.Info) (this.mixers.get(i))).equals(ad.getMixerInfo())) outputDevices.setSelectedIndex(i);
            }
            samplingRates.add(new Integer(8000));
            samplingRates.add(new Integer(11025));
            samplingRates.add(new Integer(22050));
            samplingRates.add(new Integer(32000));
            samplingRates.add(new Integer(44056));
            samplingRates.add(new Integer(44100));
            samplingRates.add(new Integer(47250));
            samplingRates.add(new Integer(48000));
            useStereo = new JCheckBox("Stereo output");
            useStereo.setSelected(ad.getChannels() == 2);
            samplingRateSlider = new JSlider(0, samplingRates.size() - 1);
            samplingRateSlider.setPaintTicks(true);
            samplingRateSlider.setSnapToTicks(true);
            samplingRateSlider.setPaintTrack(true);
            samplingRateSlider.setPaintLabels(true);
            samplingRateSlider.updateUI();
            ArrayList<Integer> sr = new ArrayList<Integer>();
            for (int i = 0; i < samplingRates.size(); ++i) sr.add(samplingRates.get(i));
            samplingRateSpinner = new JSpinner(new SpinnerListModel(sr));
            bufferSizeSlider = new JSlider(1 << 5, 1 << 16);
            bufferSizeSpinner = new JSpinner(new SpinnerNumberModel(1 << 5, 1, 1 << 16, 1));
            bufferSizeSlider.setValue(ad.getBufferSize());
            bufferSizeSpinner.setValue(new Integer(ad.getBufferSize()));
            outputIntervalSlider = new JSlider(0, bufferSizeSlider.getMaximum());
            outputIntervalSpinner = new JSpinner(new SpinnerNumberModel(ad.getOutputInterval(), 1, ad.getBufferSize(), ad.getFrameSize()));
            outputIntervalSlider.setValue(ad.getOutputInterval());
            outputIntervalSpinner.setValue(new Integer(ad.getOutputInterval()));
            samplingRateSlider.setValue(samplingRates.indexOf(Integer.valueOf(ad.getSampleRate())));
            samplingRateSpinner.setValue(Integer.valueOf(ad.getSampleRate()));
            bufferSizeSlider.setValue(ad.getBufferSize());
            bufferSizeSpinner.setValue(Integer.valueOf(ad.getBufferSize()));
            outputIntervalSlider.setValue(ad.getOutputInterval());
            outputIntervalSpinner.setValue(Integer.valueOf(ad.getOutputInterval()));
            useStereo.addChangeListener(this);
            outputDevices.addActionListener(this);
            bufferSizeSlider.addChangeListener(this);
            bufferSizeSpinner.addChangeListener(this);
            samplingRateSlider.addChangeListener(this);
            samplingRateSpinner.addChangeListener(this);
            outputIntervalSlider.addChangeListener(this);
            outputIntervalSpinner.addChangeListener(this);
            BorderLayout bl = new BorderLayout();
            dialog.setLayout(bl);
            JPanel container = new JPanel();
            container.setLayout(new GridLayout(8, 2));
            container.add(new JLabel("Output device:"));
            container.add(new JPanel());
            container.add(outputDevices);
            container.add(useStereo);
            container.add(new JLabel("Sampling rate:"));
            container.add(new JPanel());
            container.add(samplingRateSlider);
            container.add(samplingRateSpinner);
            container.add(new JLabel("Buffer size:"));
            container.add(new JPanel());
            container.add(bufferSizeSlider);
            container.add(bufferSizeSpinner);
            container.add(new JLabel("Output interval:"));
            container.add(new JPanel());
            container.add(outputIntervalSlider);
            container.add(outputIntervalSpinner);
            dialog.add(new JPanel(), BorderLayout.NORTH);
            dialog.add(new JPanel(), BorderLayout.SOUTH);
            dialog.add(new JPanel(), BorderLayout.WEST);
            dialog.add(new JPanel(), BorderLayout.EAST);
            dialog.add(container, BorderLayout.CENTER);
        }
        dialog.addComponentListener(this);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        Dimension d = owner.getSize();
        Point p = new Point();
        p.setLocation((owner.getLocation().getX() + (d.getWidth() / 2)) - (dialog.getWidth() / 2), (owner.getLocation().getY() + d.getHeight() / 2) - (dialog.getHeight() / 2));
        dialog.setLocation(p);
    }
