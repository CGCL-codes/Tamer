        public void updateFromI5D() {
            if (nChannels != i5d.getNChannels()) {
                nChannels = i5d.getNChannels();
                channelPane.getVAdjustable().setUnitIncrement(20);
                channelPanel.removeAll();
                channelPanel.setLayout(new GridLayout(nChannels, 1));
                channelActiveGroup = new CheckboxGroup();
                channelDisplayed = new Checkbox[nChannels];
                channelActive = new Checkbox[nChannels];
                for (int i = 0; i < nChannels; ++i) {
                    Panel p = new Panel(new BorderLayout());
                    channelActive[i] = new Checkbox();
                    channelActive[i].setCheckboxGroup(channelActiveGroup);
                    channelActive[i].addItemListener(this);
                    p.add(channelActive[i], BorderLayout.WEST);
                    channelDisplayed[i] = new Checkbox();
                    channelDisplayed[i].addItemListener(this);
                    p.add(channelDisplayed[i], BorderLayout.CENTER);
                    channelPanel.add(p);
                    channelActive[i].addKeyListener(win);
                    channelDisplayed[i].addKeyListener(win);
                    channelActive[i].addKeyListener(ij);
                    channelDisplayed[i].addKeyListener(ij);
                    channelActive[i].addKeyListener(win);
                    channelDisplayed[i].addKeyListener(win);
                }
                win.pack();
            }
            for (int i = 0; i < nChannels; ++i) {
                channelActive[i].setBackground(new Color(i5d.getChannelDisplayProperties(i + 1).getColorModel().getRGB(255)));
                channelActive[i].setForeground(Color.black);
                channelActive[i].repaint();
                channelDisplayed[i].setLabel(i5d.getChannelCalibration(i + 1).getLabel());
                channelDisplayed[i].setState(i5d.getChannelDisplayProperties(i + 1).isDisplayedInOverlay());
                channelDisplayed[i].setBackground(new Color(i5d.getChannelDisplayProperties(i + 1).getColorModel().getRGB(255)));
                channelDisplayed[i].setForeground(Color.black);
                channelDisplayed[i].repaint();
            }
            channelActive[(i5d.getCurrentChannel() - 1)].setState(true);
        }
