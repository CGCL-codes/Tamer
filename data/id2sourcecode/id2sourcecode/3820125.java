    public void find() {
        if (isSetup) {
            searchCount.setText(Messages.getMessage("PdfViewerSearch.ItemsFound") + " " + itemFoundCount);
            searchText.selectAll();
            searchText.grabFocus();
        } else {
            isSetup = true;
            defaultMessage = Messages.getMessage("PdfViewerSearchGUI.DefaultMessage");
            searchText = new JTextField(defaultMessage);
            searchButton = new JButton(Messages.getMessage("PdfViewerSearch.Button"));
            nav.setLayout(new BorderLayout());
            WL = new WindowListener() {

                public void windowOpened(WindowEvent arg0) {
                }

                public void windowClosing(WindowEvent arg0) {
                    removeSearchWindow(true);
                }

                public void windowClosed(WindowEvent arg0) {
                }

                public void windowIconified(WindowEvent arg0) {
                }

                public void windowDeiconified(WindowEvent arg0) {
                }

                public void windowActivated(WindowEvent arg0) {
                }

                public void windowDeactivated(WindowEvent arg0) {
                }
            };
            this.addWindowListener(WL);
            nav.add(searchButton, BorderLayout.EAST);
            nav.add(searchText, BorderLayout.CENTER);
            searchAll = new JCheckBox();
            searchAll.setSelected(true);
            searchAll.setText(Messages.getMessage("PdfViewerSearch.CheckBox"));
            nav.add(searchAll, BorderLayout.NORTH);
            itemFoundCount = 0;
            textPages.clear();
            textRectangles.clear();
            listModel = null;
            searchCount = new JTextField(Messages.getMessage("PdfViewerSearch.ItemsFound") + " " + itemFoundCount);
            searchCount.setEditable(false);
            nav.add(searchCount, BorderLayout.SOUTH);
            listModel = new DefaultListModel();
            results = new SearchList(listModel, textPages);
            results.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            ML = new MouseListener() {

                public void mouseClicked(MouseEvent arg0) {
                    if (!commonValues.isProcessing()) {
                        float scaling = currentGUI.getScaling();
                        int inset = currentGUI.getPDFDisplayInset();
                        int id = results.getSelectedIndex();
                        decode_pdf.setFoundTextAreas(null);
                        if (id != -1) {
                            Integer key = new Integer(id);
                            Object newPage = textPages.get(key);
                            if (newPage != null) {
                                int nextPage = ((Integer) newPage).intValue();
                                Rectangle highlight = (Rectangle) textRectangles.get(key);
                                if (commonValues.getCurrentPage() != nextPage) {
                                    commonValues.setCurrentPage(nextPage);
                                    currentGUI.resetStatusMessage(Messages.getMessage("PdfViewer.LoadingPage") + " " + commonValues.getCurrentPage());
                                    decode_pdf.setPageParameters(scaling, commonValues.getCurrentPage());
                                    currentGUI.decodePage(false);
                                    decode_pdf.invalidate();
                                }
                                int scrollInterval = decode_pdf.getScrollInterval();
                                int x = (int) ((highlight.x - currentGUI.cropX) * scaling) + inset;
                                int y = (int) ((currentGUI.cropH - (highlight.y - currentGUI.cropY)) * scaling) + inset;
                                int w = (int) (highlight.width * scaling);
                                int h = (int) (highlight.height * scaling);
                                Rectangle scrollto = new Rectangle(x - scrollInterval, y - h - scrollInterval, w + scrollInterval * 2, h + scrollInterval * 2);
                                decode_pdf.scrollRectToVisible(scrollto);
                                decode_pdf.setFoundTextArea(highlight);
                                decode_pdf.invalidate();
                                decode_pdf.repaint();
                            }
                        }
                    }
                }

                public void mousePressed(MouseEvent arg0) {
                }

                public void mouseReleased(MouseEvent arg0) {
                }

                public void mouseEntered(MouseEvent arg0) {
                }

                public void mouseExited(MouseEvent arg0) {
                }
            };
            results.addMouseListener(ML);
            AL = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (!isSearch) {
                        try {
                            searchText();
                        } catch (Exception e1) {
                            System.out.println("Exception " + e1);
                            e1.printStackTrace();
                        }
                    } else {
                        searcher.interrupt();
                        isSearch = false;
                        searchButton.setText(Messages.getMessage("PdfViewerSearch.Button"));
                    }
                }
            };
            searchButton.addActionListener(AL);
            searchText.selectAll();
            deleteOnClick = true;
            KL = new KeyListener() {

                public void keyTyped(KeyEvent e) {
                    if (deleteOnClick) {
                        deleteOnClick = false;
                        searchText.setText("");
                    }
                    int id = e.getID();
                    if (id == KeyEvent.KEY_TYPED) {
                        char key = e.getKeyChar();
                        if (key == '\n') {
                            try {
                                searchText();
                            } catch (Exception e1) {
                                System.out.println("Exception " + e1);
                                e1.printStackTrace();
                            }
                        }
                    }
                }

                public void keyPressed(KeyEvent arg0) {
                }

                public void keyReleased(KeyEvent arg0) {
                }
            };
            searchText.addKeyListener(KL);
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.getViewport().add(results);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(80);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(80);
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            getContentPane().add(nav, BorderLayout.NORTH);
            Container frame;
            if (commonValues.getModeOfOperation() == Values.RUNNING_APPLET) {
                frame = currentGUI.getFrame().getContentPane();
            } else {
                frame = currentGUI.getFrame();
            }
            int w = 230;
            int h = frame.getHeight();
            int x1 = frame.getLocationOnScreen().x;
            int x = frame.getWidth() + x1;
            int y = frame.getLocationOnScreen().y;
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            int width = d.width;
            if (x + w > width) {
                x = width - w;
                frame.setSize(x - x1, frame.getHeight());
            }
            setSize(w, h);
            setLocation(x, y);
            searchAll.setFocusable(false);
            searchText.grabFocus();
        }
        show();
    }
