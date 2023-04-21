        public JComponent componentFor(final Object o) {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            int numTextFields = 0;
            int row = 0;
            try {
                BeanInfo info = Introspector.getBeanInfo(o.getClass());
                PropertyDescriptor[] props = info.getPropertyDescriptors();
                for (int i = 0; i < props.length; i++) {
                    log.debug("inspecting property: " + props[i].getShortDescription());
                    final String pname = props[i].getDisplayName();
                    final Class type = props[i].getPropertyType();
                    final Method reader = props[i].getReadMethod();
                    final Method writer = props[i].getWriteMethod();
                    if (reader != null & writer != null) {
                        row++;
                        Object value = reader.invoke(o, new Object[] {});
                        log.debug("reader: " + reader.getName());
                        panel.add(new JLabel(pname + ":"), gbc(0, row));
                        if (type.equals(int.class)) {
                            final JSpinner spinner = new JSpinner();
                            spinner.setValue(value);
                            spinner.addChangeListener(new ChangeListener() {

                                public void stateChanged(ChangeEvent e) {
                                    try {
                                        log.debug("change to " + spinner.getValue());
                                        writer.invoke(o, new Object[] { spinner.getValue() });
                                    } catch (IllegalAccessException ex) {
                                    } catch (InvocationTargetException ex) {
                                    }
                                }
                            });
                            panel.add(spinner, gbc(1, row));
                        } else if (type.equals(double.class) && pname.indexOf("Fraction") >= 0) {
                            final JSpinner spinner = new JSpinner(new SpinnerNumberModel(((Double) value).doubleValue(), 0, 1.0, 0.05));
                            spinner.addChangeListener(new ChangeListener() {

                                public void stateChanged(ChangeEvent e) {
                                    try {
                                        log.debug("change to " + spinner.getValue());
                                        writer.invoke(o, new Object[] { spinner.getValue() });
                                    } catch (IllegalAccessException ex) {
                                    } catch (InvocationTargetException ex) {
                                    }
                                }
                            });
                            panel.add(spinner, gbc(1, row));
                        } else if (type.equals(double.class)) {
                            final JTextField textField = new JTextField(10);
                            textField.setText(value.toString());
                            textField.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        log.debug("change to " + textField.getText());
                                        double d = Double.parseDouble(textField.getText().trim());
                                        writer.invoke(o, new Object[] { new Double(d) });
                                    } catch (IllegalAccessException ex) {
                                    } catch (InvocationTargetException ex) {
                                    } catch (NumberFormatException ex) {
                                        log.warn("Illegal number '" + textField.getText() + "'");
                                    }
                                }
                            });
                            panel.add(textField, gbc(1, row));
                            numTextFields++;
                        } else if (type.equals(String.class)) {
                            try {
                                String allowedValueMethodName = "getAllowed" + pname.substring(0, 1).toUpperCase() + pname.substring(1) + "Values";
                                Method allowedValueMethod = o.getClass().getMethod(allowedValueMethodName, new Class[] {});
                                Object[] allowedValues = (Object[]) allowedValueMethod.invoke(o, new Object[] {});
                                final JComboBox theBox = new JComboBox();
                                theBox.addItem("-choose a value-");
                                for (int j = 0; j < allowedValues.length; j++) {
                                    theBox.addItem(allowedValues[j]);
                                }
                                theBox.setSelectedItem(value);
                                theBox.addActionListener(new ActionListener() {

                                    public void actionPerformed(ActionEvent ev) {
                                        try {
                                            if (theBox.getSelectedIndex() > 0) {
                                                writer.invoke(o, new Object[] { theBox.getSelectedItem() });
                                            } else {
                                                writer.invoke(o, new Object[] { null });
                                            }
                                        } catch (IllegalAccessException ex) {
                                            log.error(ex.toString());
                                        } catch (InvocationTargetException ex) {
                                            log.error(ex.toString());
                                        }
                                    }
                                });
                                panel.add(theBox, gbc(1, row));
                            } catch (NoSuchMethodException ex) {
                                final JTextField textField = new JTextField(10);
                                textField.setText(value == null ? "" : value.toString());
                                textField.addActionListener(new ActionListener() {

                                    public void actionPerformed(ActionEvent ev) {
                                        try {
                                            log.debug("change to " + textField.getText());
                                            writer.invoke(o, new Object[] { textField.getText() });
                                        } catch (IllegalAccessException ex) {
                                            log.error(ex.toString());
                                        } catch (InvocationTargetException ex) {
                                            log.error(ex.toString());
                                        }
                                    }
                                });
                                if (pname.indexOf("Filename") < 0) {
                                    panel.add(textField, gbc(1, row));
                                } else {
                                    final JFileChooser chooser = new JFileChooser();
                                    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                                    JButton browseButton = new JButton(new AbstractAction("Browse") {

                                        public void actionPerformed(ActionEvent ev) {
                                            int returnVal = chooser.showOpenDialog(null);
                                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                                String filename = chooser.getSelectedFile().getAbsolutePath();
                                                textField.setText(filename);
                                                try {
                                                    log.debug("change to " + textField.getText());
                                                    writer.invoke(o, new Object[] { filename });
                                                } catch (IllegalAccessException ex) {
                                                    log.error(ex.toString());
                                                } catch (InvocationTargetException ex) {
                                                    log.error(ex.toString());
                                                }
                                            }
                                        }
                                    });
                                    JPanel typeOrBrowsePanel = new JPanel();
                                    typeOrBrowsePanel.add(textField);
                                    typeOrBrowsePanel.add(browseButton);
                                    panel.add(typeOrBrowsePanel, gbc(1, row));
                                }
                                numTextFields++;
                            }
                        } else if (type.equals(boolean.class)) {
                            final JCheckBox checkbox = new JCheckBox();
                            checkbox.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent ev) {
                                    try {
                                        log.debug("change to " + checkbox.isSelected());
                                        writer.invoke(o, new Object[] { new Boolean(checkbox.isSelected()) });
                                    } catch (IllegalAccessException ex) {
                                    } catch (InvocationTargetException ex) {
                                    }
                                }
                            });
                            checkbox.setSelected(((Boolean) value).booleanValue());
                            panel.add(checkbox, gbc(1, row));
                        } else if (value != null && isValid(value.getClass())) {
                            log.debug("type " + value.getClass() + " is editable");
                            log.debug("add selector on type " + type + " of " + validSubclasses);
                            final TypeSelector selector = new TypeSelector(validSubclasses, type);
                            selector.setContent(value);
                            selector.name = name == null ? pname : name + "." + pname;
                            selector.classBox.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        Object selected = selector.instanceMap.get(selector.classBox.getSelectedItem());
                                        writer.invoke(o, new Object[] { selected });
                                    } catch (IllegalAccessException ex) {
                                    } catch (InvocationTargetException ex) {
                                    }
                                }
                            });
                            panel.add(selector, gbc(1, row));
                        } else {
                            log.debug("type " + type + " is not editable");
                            panel.add(new JLabel(value == null ? "null " : value.toString()), gbc(1, row));
                        }
                        log.debug("property " + row + "\n  name: " + name + "\n  type: " + type + "\n  value: " + value);
                        if (value != null) log.debug("class of value is: " + value.getClass()); else log.debug("null value, no class");
                    }
                }
                if (row == 0) {
                    panel.add(new JLabel("No properties to edit for class " + o.getClass()));
                }
                if (numTextFields > 0) {
                    panel.add(new JLabel("[Reminder: text will not be saved unless you use ENTER]"), gbc(0, row + 1, 2));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Editor on input " + o + ": " + e.toString());
            }
            JScrollPane scroller = new JScrollPane(panel);
            scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            return scroller;
        }
