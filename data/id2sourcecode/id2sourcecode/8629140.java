    private JPanel localFile() throws MissingComponentException {
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        JButton home = new JButton("Home");
        JButton up = new JButton("Up");
        JButton refresh = new JButton("Refresh");
        localPath = new JComboBox();
        localPath.setEditable(true);
        toolBar.add(home);
        toolBar.add(up);
        toolBar.add(refresh);
        toolBar.add(localPath);
        home.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fillList(localTable, view.getHomeDirectory());
            }
        });
        up.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fillList(localTable, view.getParentDirectory(localCurrent));
            }
        });
        localPath.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if ("comboBoxEdited".equals(e.getActionCommand())) {
                    JComboBox path = (JComboBox) e.getSource();
                    fillList(localTable, new File(path.getSelectedItem().toString()));
                }
            }
        });
        refresh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fillList(localTable, localCurrent);
            }
        });
        DefaultTableModel model = getModel(false);
        model.setColumnCount(LOCAL.length);
        localTable = new JTable(model);
        localTable.setAutoCreateRowSorter(true);
        localTable.setDragEnabled(true);
        localTable.setName(LOCAL[0]);
        new FileViewPopup(localTable, this);
        JScrollPane scrollPane = new JScrollPane(localTable);
        fillList(localTable, view.getHomeDirectory());
        TableColumnModel columnModel = localTable.getColumnModel();
        for (int i = 0; i < LOCAL.length; i++) {
            columnModel.getColumn(i).setHeaderValue(LOCAL[i]);
        }
        columnModel.getColumn(0).setCellRenderer(new TableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof File) {
                    File file = (File) value;
                    FileSystemView view = FileSystemView.getFileSystemView();
                    JLabel lable = new JLabel(view.getSystemDisplayName(file), view.getSystemIcon(file), JLabel.LEFT);
                    return lable;
                }
                return value != null ? new JLabel(value.toString()) : new JLabel();
            }
        });
        localTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                int count = e.getClickCount();
                if (count == 2) {
                    JTable table = (JTable) e.getSource();
                    int row = table.getSelectedRow();
                    String value = table.getValueAt(row, 0).toString();
                    File file = new File(value);
                    if (file.isDirectory()) {
                        fillList(table, file);
                    } else if (file.isFile()) {
                        try {
                            if (desktop != null) desktop.open(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(localTable, DnDConstants.ACTION_COPY, new DragGestureListener() {

            public void dragGestureRecognized(final DragGestureEvent e) {
                e.startDrag(DragSource.DefaultCopyDrop, new Transferable() {

                    @Override
                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return flavor == DataFlavor.stringFlavor || flavor == DataFlavor.javaFileListFlavor;
                    }

                    @Override
                    public DataFlavor[] getTransferDataFlavors() {
                        DataFlavor[] data = { DataFlavor.stringFlavor, DataFlavor.javaFileListFlavor };
                        return data;
                    }

                    @Override
                    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                        JTable table = (JTable) e.getComponent();
                        return JTableUtil.getValue(table, LOCAL[0]);
                    }
                });
            }
        });
        localTable.setTransferHandler(new TransferHandler() {

            /**
			 * 
			 */
            private static final long serialVersionUID = -4739408313189946944L;

            public boolean canImport(TransferSupport support) {
                if (!support.isDrop()) return false;
                if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) return false;
                return true;
            }

            public boolean importData(TransferSupport support) {
                if (!canImport(support)) return false;
                File file = (File) JTableUtil.getValue(localTable, LOCAL[0]);
                if (file.isFile()) {
                    file = file.getParentFile();
                }
                try {
                    Object obj = support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    if (obj instanceof UnixFile) {
                        UnixFile unix = (UnixFile) obj;
                        sftp.get(unix, file.getAbsolutePath(), transfer);
                    }
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        root.add(toolBar, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);
        return root;
    }
