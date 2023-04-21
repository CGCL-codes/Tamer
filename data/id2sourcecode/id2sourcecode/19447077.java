    void initCustom() {
        jScrollPane2.setViewportView(_comparatorPanel);
        jScrollPane2.setViewportView(jTextPane1);
        jTree1.setDragEnabled(true);
        jTree1.setDropMode(DropMode.ON);
        initTree(jTree1, sampleData());
        initTree(jTree2, sampleData());
        TransferHandler th = new TransferHandler() {

            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                final JTree tree = (c instanceof JTree) ? (JTree) c : null;
                return new Transferable() {

                    public DataFlavor[] getTransferDataFlavors() {
                        try {
                            DataFlavor df = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.lang.Object");
                            return new DataFlavor[] { df };
                        } catch (Throwable th) {
                            return null;
                        }
                    }

                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return true;
                    }

                    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                        TreePath tsp = tree.getSelectionModel().getSelectionPath();
                        if (tsp != null) {
                            return tsp.getLastPathComponent();
                        } else {
                            return null;
                        }
                    }
                };
            }

            @Override
            public boolean canImport(TransferSupport support) {
                return true;
            }

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
                return true;
            }

            @Override
            public boolean importData(TransferSupport support) {
                return super.importData(support);
            }

            @Override
            public boolean importData(JComponent comp, Transferable t) {
                Cursor oldCursor = comp.getCursor();
                comp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    for (DataFlavor df : t.getTransferDataFlavors()) {
                        try {
                            if (df.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType)) {
                                Object data = t.getTransferData(df);
                                if (data != null) {
                                    initTree(comp, data);
                                    break;
                                }
                            } else if (df.isFlavorTextType()) {
                                Object data = t.getTransferData(df);
                                if (data instanceof String) {
                                    try {
                                        URL url = new URL((String) data);
                                        InputStream is = url.openStream();
                                        File file = File.createTempFile("__cache", "___");
                                        file.deleteOnExit();
                                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file), 1024 * 10);
                                        byte[] buf = new byte[1024 * 4];
                                        int c = 0;
                                        while ((c = is.read(buf)) > 0) {
                                            os.write(buf, 0, c);
                                        }
                                        is.close();
                                        os.close();
                                        if (loadData(comp, url.toString(), file)) {
                                            _files.clear();
                                            _files.add(file);
                                            break;
                                        }
                                    } catch (Throwable th) {
                                        th.printStackTrace();
                                    }
                                }
                            } else {
                                Object data = t.getTransferData(df);
                                if (data instanceof List && ((List) data).size() > 0) {
                                    List list = (List) data;
                                    Object item = list.get(0);
                                    if (item instanceof File) {
                                        if (loadData(comp, ((File) item).getAbsolutePath(), (File) item)) {
                                            _files.clear();
                                            _files.add((File) item);
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (UnsupportedFlavorException ufex) {
                        } catch (IOException ioex) {
                        }
                    }
                } finally {
                    comp.setCursor(oldCursor);
                }
                return true;
            }
        };
        jTree1.setTransferHandler(th);
        jTree2.setTransferHandler(th);
        TreeSelectionListener tsl = new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                Object a = null;
                Object b = null;
                Object c = null;
                JTree tree = (JTree) e.getSource();
                TreeSelectionModel tsm = tree.getSelectionModel();
                for (TreePath tp : tsm.getSelectionPaths()) {
                    if (tp.getPathCount() > 0) {
                        if (a == null) {
                            a = tp;
                        } else if (b == null) {
                            b = tp;
                        } else {
                            c = tp;
                        }
                    }
                }
                if (c != null) {
                    return;
                }
                if (a != null) {
                    a = ((TreePath) a).getPathComponent(((TreePath) a).getPathCount() - 1);
                    if (a instanceof DefaultMutableTreeNode) {
                        a = ((DefaultMutableTreeNode) a).getUserObject();
                    }
                }
                if (b != null) {
                    b = ((TreePath) b).getPathComponent(((TreePath) b).getPathCount() - 1);
                    if (b instanceof DefaultMutableTreeNode) {
                        b = ((DefaultMutableTreeNode) b).getUserObject();
                    }
                }
                if (b != null) {
                    try {
                        if (a instanceof StructureTreeModel.PairInTree) {
                            a = ((StructureTreeModel.PairInTree) a).getB();
                        }
                        if (b instanceof StructureTreeModel.PairInTree) {
                            b = ((StructureTreeModel.PairInTree) b).getB();
                        }
                        setObjectA(a);
                        setObjectB(b);
                        if (jCheckBoxCOMPARE.isSelected()) {
                            initTextArea(getObjectA(), getObjectB());
                        } else {
                            setObject(b);
                            initTextArea(getObject());
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
                    try {
                        if (a instanceof StructureTreeModel.PairInTree) {
                            a = ((StructureTreeModel.PairInTree) a).getB();
                        }
                        setObject(a);
                        if (tree.equals(jTree1)) {
                            setObjectA(a);
                        } else {
                            setObjectB(a);
                        }
                        if (jCheckBoxCOMPARE.isSelected()) {
                            initTextArea(getObjectA(), getObjectB());
                        } else {
                            initTextArea(getObject());
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        };
        jTree1.addTreeSelectionListener(tsl);
        jTree2.addTreeSelectionListener(tsl);
        TreeCellEditor tce1 = new DefaultTreeCellEditor(jTree1, new DefaultTreeCellRenderer()) {

            @Override
            public boolean isCellEditable(EventObject event) {
                JTree tree = (JTree) event.getSource();
                Object obj = tree.getSelectionPath().getLastPathComponent();
                if (obj instanceof DefaultMutableTreeNode) {
                    obj = ((DefaultMutableTreeNode) obj).getUserObject();
                }
                return Utilities.isScalarOrString(obj);
            }
        };
        TreeCellEditor tce2 = new DefaultTreeCellEditor(jTree1, new DefaultTreeCellRenderer());
        jTree1.setCellEditor(tce1);
        jTree2.setCellEditor(tce2);
        TreeManipulator tm = new TreeManipulator();
        jTree1.addTreeWillExpandListener(tm);
        jTree1.addKeyListener(tm);
        ChangeListener buttonChangeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                JComponent comp = (JComponent) e.getSource();
                Cursor oldCursor = comp.getCursor();
                if (e.getSource() instanceof JCheckBox || (e.getSource() instanceof JRadioButton && ((JRadioButton) e.getSource()).isSelected())) {
                    try {
                        comp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        if (!jCheckBoxCOMPARE.isSelected()) {
                            initTextArea(getObject());
                        } else {
                            initTextArea(getObjectA(), getObjectB());
                        }
                    } finally {
                        comp.setCursor(oldCursor);
                    }
                }
            }
        };
        jCheckBoxIndented.addChangeListener(buttonChangeListener);
        for (AbstractButton b : Collections.list(buttonGroupFormat.getElements())) {
            b.addChangeListener(buttonChangeListener);
        }
        jCheckBoxCOMPARE.addChangeListener(buttonChangeListener);
        jCheckBoxIdentity.addChangeListener(buttonChangeListener);
        for (AbstractButton b : Collections.list(buttonGroupCompareMode.getElements())) {
            b.addChangeListener(buttonChangeListener);
        }
        KeyListener kl = new KeyListener() {

            boolean _flag = false;

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        jTextPane1.addKeyListener(kl);
        jTextPane1.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                JTextPane pane = (JTextPane) e.getSource();
                try {
                    Field gc = pane.getDocument().getClass().getSuperclass().getDeclaredField("data");
                    gc.setAccessible(true);
                    if (gc.get(pane.getDocument()) instanceof StructureContent) {
                        StructureContent c = (StructureContent) gc.get(pane.getDocument());
                        List<Pair<Object, Pair<Position, Position>>> lst = c.findObjectsForPosition(pane.getCaretPosition());
                        System.out.println("\nCaret position: " + pane.getCaretPosition());
                        for (Pair<Object, Pair<Position, Position>> p : lst) {
                            System.out.println("  " + p.getB().getA().getOffset() + "-" + p.getB().getB().getOffset() + " : " + p.getA());
                        }
                        pane.getHighlighter().removeAllHighlights();
                        if (!lst.isEmpty()) {
                            Pair<Object, Pair<Position, Position>> p = lst.get(0);
                            System.out.println("\nPositions info:\n" + c.dumpObjectPositions(p.getA()));
                            pane.getHighlighter().addHighlight(p.getB().getA().getOffset() + 1, p.getB().getB().getOffset() + 1, (new StructureStyles()).getHighlighter(COMPARE_STATUS.mismatch));
                            pane.invalidate();
                            pane.repaint();
                        }
                    }
                } catch (Throwable th) {
                    th.printStackTrace(System.out);
                }
            }
        });
        kl = new KeyListener() {

            boolean _flag = false;

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 82 && e.isControlDown() && !_flag) {
                    _flag = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 82 && e.isControlDown() && _flag) {
                    _flag = false;
                    showHighlights(_comparatorWriter.createLineComparisonHighlights(), _comparatorPanel.getLeft().getDocument());
                }
            }
        };
        _comparatorPanel.getLeft().addKeyListener(kl);
        _comparatorPanel.getRight().addKeyListener(kl);
        _comparatorPanel.getLeft().setMinimumSize(new Dimension(0, 0));
        _comparatorPanel.getRight().setMinimumSize(new Dimension(0, 0));
    }
