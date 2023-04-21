    private void installBean(JComponent bean) {
        try {
            componentPanel.removeAll();
            componentPanel.add(bean);
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), bean.getClass().getSuperclass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            propertyPanel.removeAll();
            GridBagLayout gridbag = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            propertyPanel.setLayout(gridbag);
            int count = 0;
            String[] types = new String[] { "class java.util.Locale", "boolean", "int", "class java.awt.Color", "class java.util.Date", "class java.lang.String" };
            for (int t = 0; t < types.length; t++) {
                for (int i = 0; i < propertyDescriptors.length; i++) {
                    if (propertyDescriptors[i].getWriteMethod() != null) {
                        String type = propertyDescriptors[i].getPropertyType().toString();
                        final PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
                        final JComponent currentBean = bean;
                        final Method readMethod = propertyDescriptor.getReadMethod();
                        final Method writeMethod = propertyDescriptor.getWriteMethod();
                        if (type.equals(types[t]) && (((readMethod != null) && (writeMethod != null)) || ("class java.util.Locale".equals(type)))) {
                            if ("boolean".equals(type)) {
                                boolean isSelected = false;
                                try {
                                    Boolean booleanObj = ((Boolean) readMethod.invoke(bean, null));
                                    isSelected = booleanObj.booleanValue();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                final JCheckBox checkBox = new JCheckBox("", isSelected);
                                checkBox.addActionListener(new ActionListener() {

                                    public void actionPerformed(ActionEvent event) {
                                        try {
                                            if (checkBox.isSelected()) {
                                                writeMethod.invoke(currentBean, new Object[] { new Boolean(true) });
                                            } else {
                                                writeMethod.invoke(currentBean, new Object[] { new Boolean(false) });
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                addProperty(propertyDescriptors[i], checkBox, gridbag);
                                count += 1;
                            } else if ("int".equals(type)) {
                                JSpinField spinField = new JSpinField();
                                spinField.addPropertyChangeListener(new PropertyChangeListener() {

                                    public void propertyChange(PropertyChangeEvent evt) {
                                        try {
                                            if (evt.getPropertyName().equals("value")) {
                                                writeMethod.invoke(currentBean, new Object[] { evt.getNewValue() });
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                                try {
                                    Integer integerObj = ((Integer) readMethod.invoke(bean, null));
                                    spinField.setValue(integerObj.intValue());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                addProperty(propertyDescriptors[i], spinField, gridbag);
                                count += 1;
                            } else if ("class java.lang.String".equals(type)) {
                                String string = "";
                                try {
                                    string = ((String) readMethod.invoke(bean, null));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                JTextField textField = new JTextField(string);
                                ActionListener actionListener = new ActionListener() {

                                    public void actionPerformed(ActionEvent e) {
                                        System.out.println("JCalendarDemo.installBean(): " + e);
                                        try {
                                            writeMethod.invoke(currentBean, new Object[] { e.getActionCommand() });
                                        } catch (Exception ex) {
                                        }
                                    }
                                };
                                textField.addActionListener(actionListener);
                                addProperty(propertyDescriptors[i], textField, gridbag);
                                count += 1;
                            } else if ("class java.util.Locale".equals(type)) {
                                JLocaleChooser localeChooser = new JLocaleChooser(bean);
                                localeChooser.setPreferredSize(new Dimension(200, localeChooser.getPreferredSize().height));
                                addProperty(propertyDescriptors[i], localeChooser, gridbag);
                                count += 1;
                            } else if ("class java.util.Date".equals(type)) {
                                JDateChooser dateChooser = new JDateChooser();
                                dateChooser.addPropertyChangeListener((PropertyChangeListener) bean);
                                addProperty(propertyDescriptors[i], dateChooser, gridbag);
                                count += 1;
                            } else if ("class java.awt.Color".equals(type)) {
                                final JButton button = new JButton();
                                try {
                                    final Color colorObj = ((Color) readMethod.invoke(bean, null));
                                    button.setText("...");
                                    button.setBackground(colorObj);
                                    ActionListener actionListener = new ActionListener() {

                                        public void actionPerformed(ActionEvent e) {
                                            Color newColor = JColorChooser.showDialog(JCalendarDemo.this, "Choose Color", colorObj);
                                            button.setBackground(newColor);
                                            try {
                                                writeMethod.invoke(currentBean, new Object[] { newColor });
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    };
                                    button.addActionListener(actionListener);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                addProperty(propertyDescriptors[i], button, gridbag);
                                count += 1;
                            }
                        }
                    }
                }
            }
            URL iconURL = bean.getClass().getResource("images/" + bean.getName() + "Color16.gif");
            ImageIcon icon = new ImageIcon(iconURL);
            componentTitlePanel.setTitle(bean.getName(), icon);
            bean.validate();
            propertyPanel.validate();
            componentPanel.validate();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }
