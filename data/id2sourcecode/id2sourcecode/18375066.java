    protected void registerWidgets() {
        registerBeanFactory("action", DefaultAction.class);
        registerFactory("boxLayout", new Factory() {

            public Object newInstance(Map properties) throws InstantiationException, InstantiationException, IllegalAccessException {
                return createBoxLayout(properties);
            }
        });
        registerBeanFactory("button", JButton.class);
        registerBeanFactory("buttonGroup", ButtonGroup.class);
        registerBeanFactory("checkBox", JCheckBox.class);
        registerBeanFactory("checkBoxMenuItem", JCheckBoxMenuItem.class);
        registerFactory("comboBox", new Factory() {

            public Object newInstance(Map properties) throws InstantiationException, InstantiationException, IllegalAccessException {
                return createComboBox(properties);
            }
        });
        registerBeanFactory("desktopPane", JDesktopPane.class);
        registerFactory("dialog", new Factory() {

            public Object newInstance(Map properties) throws InstantiationException, InstantiationException, IllegalAccessException {
                return createDialog(properties);
            }
        });
        registerBeanFactory("editorPane", JEditorPane.class);
        registerBeanFactory("fileChooser", JFileChooser.class);
        registerBeanFactory("frame", JFrame.class);
        registerBeanFactory("internalFrame", JInternalFrame.class);
        registerBeanFactory("label", JLabel.class);
        registerBeanFactory("list", JList.class);
        registerFactory("map", new Factory() {

            public Object newInstance(Map properties) throws InstantiationException, InstantiationException, IllegalAccessException {
                return properties;
            }
        });
        registerBeanFactory("menu", JMenu.class);
        registerBeanFactory("menuBar", JMenuBar.class);
        registerBeanFactory("menuItem", JMenuItem.class);
        registerBeanFactory("panel", JPanel.class);
        registerBeanFactory("passwordField", JPasswordField.class);
        registerBeanFactory("popupMenu", JPopupMenu.class);
        registerBeanFactory("progressBar", JProgressBar.class);
        registerBeanFactory("radioButton", JRadioButton.class);
        registerBeanFactory("radioButtonMenuItem", JRadioButtonMenuItem.class);
        registerBeanFactory("optionPane", JOptionPane.class);
        registerBeanFactory("scrollPane", JScrollPane.class);
        registerBeanFactory("separator", JSeparator.class);
        registerFactory("splitPane", new Factory() {

            public Object newInstance(Map properties) {
                JSplitPane answer = new JSplitPane();
                answer.setLeftComponent(null);
                answer.setRightComponent(null);
                answer.setTopComponent(null);
                answer.setBottomComponent(null);
                return answer;
            }
        });
        registerFactory("hbox", new Factory() {

            public Object newInstance(Map properties) {
                return Box.createHorizontalBox();
            }
        });
        registerFactory("vbox", new Factory() {

            public Object newInstance(Map properties) {
                return Box.createVerticalBox();
            }
        });
        registerBeanFactory("tabbedPane", JTabbedPane.class);
        registerBeanFactory("table", JTable.class);
        registerBeanFactory("textArea", JTextArea.class);
        registerBeanFactory("textPane", JTextPane.class);
        registerBeanFactory("textField", JTextField.class);
        registerBeanFactory("toggleButton", JToggleButton.class);
        registerBeanFactory("tree", JTree.class);
        registerBeanFactory("toolBar", JToolBar.class);
        registerFactory("tableModel", new Factory() {

            public Object newInstance(Map properties) {
                ValueModel model = (ValueModel) properties.remove("model");
                if (model == null) {
                    Object list = properties.remove("list");
                    if (list == null) {
                        list = new ArrayList();
                    }
                    model = new ValueHolder(list);
                }
                return new DefaultTableModel(model);
            }
        });
        registerFactory("propertyColumn", new Factory() {

            public Object newInstance(Map properties) {
                Object current = getCurrent();
                if (current instanceof DefaultTableModel) {
                    DefaultTableModel model = (DefaultTableModel) current;
                    Object header = properties.remove("header");
                    if (header == null) {
                        header = "";
                    }
                    String property = (String) properties.remove("propertyName");
                    if (property == null) {
                        throw new IllegalArgumentException("Must specify a property for a propertyColumn");
                    }
                    Class type = (Class) properties.remove("type");
                    if (type == null) {
                        type = Object.class;
                    }
                    return model.addPropertyColumn(header, property, type);
                } else {
                    throw new RuntimeException("propertyColumn must be a child of a tableModel");
                }
            }
        });
        registerFactory("closureColumn", new Factory() {

            public Object newInstance(Map properties) {
                Object current = getCurrent();
                if (current instanceof DefaultTableModel) {
                    DefaultTableModel model = (DefaultTableModel) current;
                    Object header = properties.remove("header");
                    if (header == null) {
                        header = "";
                    }
                    Closure readClosure = (Closure) properties.remove("read");
                    if (readClosure == null) {
                        throw new IllegalArgumentException("Must specify 'read' Closure property for a closureColumn");
                    }
                    Closure writeClosure = (Closure) properties.remove("write");
                    Class type = (Class) properties.remove("type");
                    if (type == null) {
                        type = Object.class;
                    }
                    return model.addClosureColumn(header, readClosure, writeClosure, type);
                } else {
                    throw new RuntimeException("propertyColumn must be a child of a tableModel");
                }
            }
        });
        registerBeanFactory("tableLayout", TableLayout.class);
        registerFactory("tr", new Factory() {

            public Object newInstance(Map properties) {
                Object parent = getCurrent();
                if (parent instanceof TableLayout) {
                    return new TableLayoutRow((TableLayout) parent);
                } else {
                    throw new RuntimeException("'tr' must be within a 'tableLayout'");
                }
            }
        });
        registerFactory("td", new Factory() {

            public Object newInstance(Map properties) {
                Object parent = getCurrent();
                if (parent instanceof TableLayoutRow) {
                    return new TableLayoutCell((TableLayoutRow) parent);
                } else {
                    throw new RuntimeException("'td' must be within a 'tr'");
                }
            }
        });
    }
