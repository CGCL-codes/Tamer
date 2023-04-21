            public Object construct() {
                doClose(RET_OK);
                com.javable.dataview.DataChannel xchannel = null;
                com.javable.dataview.DataChannel xychannel = null;
                com.javable.dataview.DataChannel yychannel = null;
                ifr = (DataFrame) desktop.getActiveFrame();
                if (ifr != null) {
                    iv = ifr.getIVContent();
                    if ((iv == null) || replaceRadioButton.isSelected()) {
                        iv = new ViewContent();
                        ifr.setIVContent(iv);
                    }
                    DataView view = ifr.getDataContent().getView();
                    source = view.getStorage();
                    result = iv.getStorage();
                    double r[] = regionSelector.getLimits(view);
                    xmin = r[0];
                    xmax = r[1];
                    String inf = "";
                    inf = "Cursor " + cursorComboBox.getSelectedItem() + ", Channel " + xchanTextField.getValue();
                    xres = new DataChannel(inf, source.getGroupsSize());
                    if (valueComboBox.getSelectedIndex() == 0) {
                        inf = valueComboBox.getSelectedItem() + " ";
                    } else {
                        inf = valueComboBox.getSelectedItem() + " " + regionSelector.getRegionComboBox().getSelectedItem();
                    }
                    inf = inf + ", Channel " + ychanTextField.getValue();
                    yres = new DataChannel(inf, source.getGroupsSize());
                    try {
                        for (int i = 0; i < source.getGroupsSize(); i++) {
                            DataGroup group = source.getGroup(i);
                            xchannel = source.getChannel(i, group.getXChannel());
                            xychannel = source.getChannel(i, xchanTextField.getValue());
                            yychannel = source.getChannel(i, ychanTextField.getValue());
                            if ((xchannel != null) && (xychannel != null) && (yychannel != null) && (xychannel.getAttribute().isNormal()) && (yychannel.getAttribute().isNormal())) {
                                if (i == 0) xres.setUnits(xychannel.getUnits());
                                xres.setData(i, ChannelStats.getValueAtX(view.getCursors().getCursorSlider().getValueAt(cursorComboBox.getSelectedIndex()), xchannel, xychannel));
                                if (i == 0) yres.setUnits(yychannel.getUnits());
                                if (valueComboBox.getSelectedIndex() == 0) {
                                    yres.setData(i, ChannelStats.getValueAtX(view.getCursors().getCursorSlider().getValueAt(0), xchannel, yychannel));
                                } else {
                                    yres.setData(i, getStat(valueComboBox.getSelectedIndex(), xchannel, yychannel));
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                return ResourceManager.getResource("Done");
            }
