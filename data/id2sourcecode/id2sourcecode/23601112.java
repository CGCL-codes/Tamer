    private void buildMenu(final SpidersGraph spiders) {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem, subMenuItem;
        JRadioButtonMenuItem rbMenuItem;
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menu = new JMenu(MindRaiderConstants.MR_TITLE);
        menu.setMnemonic(KeyEvent.VK_M);
        ButtonGroup perspectiveGroup = new ButtonGroup();
        submenu = new JMenu(Messages.getString("MindRaiderJFrame.setPerspective"));
        submenu.setMnemonic(KeyEvent.VK_P);
        subMenuItem = new JRadioButtonMenuItem(Messages.getString("MindRaiderJFrame.outliner"));
        subMenuItem.setEnabled(true);
        if (MindRaider.OUTLINER_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setPerspective(MindRaider.OUTLINER_PERSPECTIVE);
            }
        });
        submenu.add(subMenuItem);
        perspectiveGroup.add(subMenuItem);
        subMenuItem = new JRadioButtonMenuItem(Messages.getString("MindRaiderJFrame.semanticWeb"));
        subMenuItem.setEnabled(true);
        if (MindRaider.SW_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setPerspective(MindRaider.SW_PERSPECTIVE);
            }
        });
        submenu.add(subMenuItem);
        perspectiveGroup.add(subMenuItem);
        subMenuItem = new JRadioButtonMenuItem(Messages.getString("MindRaiderJFrame.experimentalFeatures"));
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setPerspective(MindRaider.EXPERIMENTAL_PERSPECTIVE);
            }
        });
        submenu.add(subMenuItem);
        perspectiveGroup.add(subMenuItem);
        menu.add(submenu);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.preferences"));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new PreferencesJDialog();
            }
        });
        menu.add(menuItem);
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu.addSeparator();
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.folders"));
            menuItem.setMnemonic(KeyEvent.VK_F);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.notebooks"));
            menuItem.setMnemonic(KeyEvent.VK_N);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.facets"));
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.attachments"));
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.channels"));
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        MindRaider.notebookCustodian.loadNotebook(new URI(Messages.getString("MindRaiderJFrame.channels")));
                    } catch (URISyntaxException e1) {
                        logger.error("actionPerformed(ActionEvent)", e1);
                    }
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.bookmarks"));
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        MindRaider.notebookCustodian.loadNotebook(new URI(Messages.getString("MindRaiderJFrame.bookmarks")));
                    } catch (URISyntaxException e1) {
                        logger.error("actionPerformed(ActionEvent)", e1);
                    }
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.associations"));
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.taxonomies"));
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuBar.add(menu);
        }
        menu.addSeparator();
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.setActiveNotebookAsHome"));
        menuItem.setMnemonic(KeyEvent.VK_H);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.profile.setHomeNotebook();
            }
        });
        menu.add(menuItem);
        menu.add(menuItem);
        if (MindRaider.SW_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.myProfile"));
            menuItem.setMnemonic(KeyEvent.VK_M);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    MindRaider.notebookCustodian.close();
                    NotebookOutlineJPanel.getInstance().clear();
                    String myProfile = MindRaider.profile.getProfileLocation();
                    StatusBar.show(Messages.getString("MindRaiderJFrame.loadingProfile", myProfile));
                    try {
                        MindRaider.spidersGraph.load(myProfile);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(MindRaiderJFrame.this, Messages.getString("MindRaiderJFrame.unableToLoadProfile", e1.getMessage()), Messages.getString("MindRaiderJFrame.loadModelError"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            menu.add(menuItem);
        }
        menu.addSeparator();
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.exit"), KeyEvent.VK_X);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exitMindRaider();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu(Messages.getString("MindRaiderJFrame.search"));
        menu.setMnemonic(KeyEvent.VK_F);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.searchNotebooks"));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new OpenNotebookJDialog();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.searchFulltext"));
        menuItem.setMnemonic(KeyEvent.VK_F);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new FtsJDialog();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.searchConceptsInNotebook"));
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (MindRaider.profile.getActiveNotebookUri() != null) {
                    new OpenConceptJDialog();
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.searchConceptsByTag"));
        menuItem.setEnabled(true);
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new OpenConceptByTagJDialog();
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.rebuildSearchIndex"));
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SearchCommander.rebuildSearchAndTagIndices();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu(Messages.getString("MindRaiderJFrame.view"));
        menu.setMnemonic(KeyEvent.VK_V);
        ButtonGroup lfGroup = new ButtonGroup();
        submenu = new JMenu(Messages.getString("MindRaiderJFrame.lookAndFeel"));
        logger.debug("Look and feel is: " + MindRaider.profile.getLookAndFeel());
        submenu.setMnemonic(KeyEvent.VK_L);
        subMenuItem = new JRadioButtonMenuItem(Messages.getString("MindRaiderJFrame.lookAndFeelNative"));
        if (MindRaider.LF_NATIVE.equals(MindRaider.profile.getLookAndFeel())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setLookAndFeel(MindRaider.LF_NATIVE);
            }
        });
        submenu.add(subMenuItem);
        lfGroup.add(subMenuItem);
        subMenuItem = new JRadioButtonMenuItem(Messages.getString("MindRaiderJFrame.lookAndFeelJava"));
        if (MindRaider.LF_JAVA_DEFAULT.equals(MindRaider.profile.getLookAndFeel())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setLookAndFeel(MindRaider.LF_JAVA_DEFAULT);
            }
        });
        submenu.add(subMenuItem);
        lfGroup.add(subMenuItem);
        subMenuItem = new JRadioButtonMenuItem(Messages.getString("MindRaiderJFrame.lookAndFeelDarkJava"));
        if (MindRaider.LF_JAVA_BLACK.equals(MindRaider.profile.getLookAndFeel())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setLookAndFeel(MindRaider.LF_JAVA_BLACK);
            }
        });
        submenu.add(subMenuItem);
        lfGroup.add(subMenuItem);
        menu.add(submenu);
        menu.addSeparator();
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.leftSideBar"));
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (leftSidebarSplitPane.getDividerLocation() == 1) {
                    leftSidebarSplitPane.resetToPreferredSizes();
                } else {
                    closeLeftSidebar();
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.rightSideBar"));
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().toggleRightSidebar();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.toolbar"));
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.masterToolBar.toggleVisibility();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.rdfNavigatorDashboard"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.spidersGraph.getGlPanel().toggleControlPanel();
            }
        });
        menu.add(menuItem);
        JCheckBoxMenuItem checkboxMenuItem;
        ButtonGroup colorSchemeGroup;
        if (!MindRaider.OUTLINER_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu.addSeparator();
            submenu = new JMenu(Messages.getString("MindRaiderJFrame.facet"));
            submenu.setMnemonic(KeyEvent.VK_F);
            colorSchemeGroup = new ButtonGroup();
            String[] facetLabels = FacetCustodian.getInstance().getFacetLabels();
            if (!ArrayUtils.isEmpty(facetLabels)) {
                for (String facetLabel : facetLabels) {
                    rbMenuItem = new JRadioButtonMenuItem(facetLabel);
                    rbMenuItem.addActionListener(new FacetActionListener(facetLabel));
                    colorSchemeGroup.add(rbMenuItem);
                    submenu.add(rbMenuItem);
                    if (BriefFacet.LABEL.equals(facetLabel)) {
                        rbMenuItem.setSelected(true);
                    }
                }
            }
            menu.add(submenu);
            checkboxMenuItem = new JCheckBoxMenuItem(Messages.getString("MindRaiderJFrame.graphLabelAsUri"));
            checkboxMenuItem.setMnemonic(KeyEvent.VK_G);
            checkboxMenuItem.setState(MindRaider.spidersGraph.isUriLabels());
            checkboxMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JCheckBoxMenuItem) {
                        JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                        MindRaider.spidersGraph.setUriLabels(j.getState());
                        MindRaider.spidersGraph.renderModel();
                        MindRaider.profile.setGraphShowLabelsAsUris(j.getState());
                        MindRaider.profile.save();
                    }
                }
            });
            menu.add(checkboxMenuItem);
            checkboxMenuItem = new JCheckBoxMenuItem(Messages.getString("MindRaiderJFrame.predicateNodes"));
            checkboxMenuItem.setMnemonic(KeyEvent.VK_P);
            checkboxMenuItem.setState(!MindRaider.spidersGraph.getHidePredicates());
            checkboxMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JCheckBoxMenuItem) {
                        JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                        MindRaider.spidersGraph.hidePredicates(!j.getState());
                        MindRaider.spidersGraph.renderModel();
                        MindRaider.profile.setGraphHidePredicates(!j.getState());
                        MindRaider.profile.save();
                    }
                }
            });
            menu.add(checkboxMenuItem);
            checkboxMenuItem = new JCheckBoxMenuItem(Messages.getString("MindRaiderJFrame.multilineLabels"));
            checkboxMenuItem.setMnemonic(KeyEvent.VK_M);
            checkboxMenuItem.setState(MindRaider.spidersGraph.isMultilineNodes());
            checkboxMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JCheckBoxMenuItem) {
                        JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                        MindRaider.spidersGraph.setMultilineNodes(j.getState());
                        MindRaider.spidersGraph.renderModel();
                        MindRaider.profile.setGraphMultilineLabels(j.getState());
                        MindRaider.profile.save();
                    }
                }
            });
            menu.add(checkboxMenuItem);
        }
        menu.addSeparator();
        checkboxMenuItem = new JCheckBoxMenuItem(Messages.getString("MindRaiderJFrame.antiAliased"), true);
        checkboxMenuItem.setMnemonic(KeyEvent.VK_A);
        checkboxMenuItem.setState(SpidersGraph.antialiased);
        checkboxMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                    SpidersGraph.antialiased = j.getState();
                    MindRaider.spidersGraph.renderModel();
                }
            }
        });
        menu.add(checkboxMenuItem);
        checkboxMenuItem = new JCheckBoxMenuItem(Messages.getString("MindRaiderJFrame.hyperbolic"), true);
        checkboxMenuItem.setMnemonic(KeyEvent.VK_H);
        checkboxMenuItem.setState(SpidersGraph.hyperbolic);
        checkboxMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                    SpidersGraph.hyperbolic = j.getState();
                    MindRaider.spidersGraph.renderModel();
                }
            }
        });
        menu.add(checkboxMenuItem);
        checkboxMenuItem = new JCheckBoxMenuItem(Messages.getString("MindRaiderJFrame.fps"), true);
        checkboxMenuItem.setMnemonic(KeyEvent.VK_F);
        checkboxMenuItem.setState(SpidersGraph.fps);
        checkboxMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                    SpidersGraph.fps = j.getState();
                    MindRaider.spidersGraph.renderModel();
                }
            }
        });
        menu.add(checkboxMenuItem);
        submenu = new JMenu(Messages.getString("MindRaiderJFrame.colorScheme"));
        submenu.setMnemonic(KeyEvent.VK_C);
        String[] allProfilesUris = MindRaider.spidersColorProfileRegistry.getAllProfilesUris();
        colorSchemeGroup = new ButtonGroup();
        for (int i = 0; i < allProfilesUris.length; i++) {
            rbMenuItem = new UriJRadioButtonMenuItem(MindRaider.spidersColorProfileRegistry.getColorProfileByUri(allProfilesUris[i]).getLabel(), allProfilesUris[i]);
            rbMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof UriJRadioButtonMenuItem) {
                        MindRaider.spidersColorProfileRegistry.setCurrentProfile(((UriJRadioButtonMenuItem) e.getSource()).uri);
                        MindRaider.spidersGraph.setRenderingProfile(MindRaider.spidersColorProfileRegistry.getCurrentProfile());
                        MindRaider.spidersGraph.renderModel();
                    }
                }
            });
            colorSchemeGroup.add(rbMenuItem);
            submenu.add(rbMenuItem);
        }
        menu.add(submenu);
        submenu = new JMenu(Messages.getString("MindRaiderJFrame.colorSchemeAnnotation"));
        submenu.setMnemonic(KeyEvent.VK_A);
        allProfilesUris = MindRaider.annotationColorProfileRegistry.getAllProfilesUris();
        colorSchemeGroup = new ButtonGroup();
        for (int i = 0; i < allProfilesUris.length; i++) {
            rbMenuItem = new UriJRadioButtonMenuItem(MindRaider.annotationColorProfileRegistry.getColorProfileByUri(allProfilesUris[i]).getLabel(), allProfilesUris[i]);
            rbMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof UriJRadioButtonMenuItem) {
                        MindRaider.annotationColorProfileRegistry.setCurrentProfile(((UriJRadioButtonMenuItem) e.getSource()).uri);
                        NotebookOutlineJPanel.getInstance().conceptJPanel.refresh();
                    }
                }
            });
            colorSchemeGroup.add(rbMenuItem);
            submenu.add(rbMenuItem);
        }
        menu.add(submenu);
        menu.addSeparator();
        checkboxMenuItem = new JCheckBoxMenuItem(Messages.getString("MindRaiderJFrame.fullScreen"));
        checkboxMenuItem.setMnemonic(KeyEvent.VK_U);
        checkboxMenuItem.setState(false);
        checkboxMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                    if (j.getState()) {
                        Gfx.toggleFullScreen(MindRaiderJFrame.this);
                    } else {
                        Gfx.toggleFullScreen(null);
                    }
                }
            }
        });
        menu.add(checkboxMenuItem);
        menuBar.add(menu);
        menu = new JMenu(Messages.getString("MindRaiderJFrame.folder"));
        menu.setMnemonic(KeyEvent.VK_F);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.new"));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new NewFolderJDialog();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.discard"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(MindRaiderJFrame.this, Messages.getString("MindRaiderJFrame.confirmDiscardFolder", MindRaider.profile.getActiveNotebook()));
                if (result == JOptionPane.YES_OPTION) {
                }
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu(Messages.getString("MindRaiderJFrame.notebook"));
        menu.setMnemonic(KeyEvent.VK_N);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.new"));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new NewNotebookJDialog();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.open"));
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new OpenNotebookJDialog();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.close"));
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.notebookCustodian.close();
                NotebookOutlineJPanel.getInstance().refresh();
                MindRaider.spidersGraph.renderModel();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.discard"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(MindRaiderJFrame.this, Messages.getString("MindRaiderJFrame.confirmDiscardNotebook", MindRaider.profile.getActiveNotebook()));
                if (result == JOptionPane.YES_OPTION) {
                    if (MindRaider.profile.getActiveNotebookUri() != null) {
                        try {
                            MindRaider.folderCustodian.discardNotebook(MindRaider.profile.getActiveNotebookUri().toString());
                            MindRaider.notebookCustodian.close();
                        } catch (Exception e1) {
                            logger.error(Messages.getString("MindRaiderJFrame.unableToDiscardNotebook"), e1);
                        }
                    }
                }
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.up"));
        menuItem.setMnemonic(KeyEvent.VK_U);
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.down"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        submenu = new JMenu(Messages.getString("MindRaiderJFrame.export"));
        submenu.setMnemonic(KeyEvent.VK_E);
        subMenuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.opml"));
        subMenuItem.setSelected(true);
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (MindRaider.profile.getActiveNotebook() == null) {
                    JOptionPane.showMessageDialog(MindRaiderJFrame.this, Messages.getString("MindRaiderJFrame.exportNotebookWarning"), Messages.getString("MindRaiderJFrame.exportError"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JFileChooser fc = new JFileChooser();
                fc.setApproveButtonText(Messages.getString("MindRaiderJFrame.export"));
                fc.setControlButtonsAreShown(true);
                fc.setDialogTitle(Messages.getString("MindRaiderJFrame.chooseExportDirectory"));
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                String exportDirectory = MindRaider.profile.getHomeDirectory() + File.separator + "export" + File.separator + "opml";
                Utils.createDirectory(exportDirectory);
                fc.setCurrentDirectory(new File(exportDirectory));
                int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String dstFileName = fc.getSelectedFile().getAbsolutePath() + File.separator + "OPML-EXPORT-" + MindRaider.notebookCustodian.getActiveNotebookNcName() + ".xml";
                    logger.debug(Messages.getString("MindRaiderJFrame.exportingToFile", dstFileName));
                    MindRaider.notebookCustodian.exportNotebook(NotebookCustodian.FORMAT_OPML, dstFileName);
                    Launcher.launchViaStart(dstFileName);
                } else {
                    logger.debug(Messages.getString("MindRaiderJFrame.exportCommandCancelledByUser"));
                }
            }
        });
        submenu.add(subMenuItem);
        subMenuItem = new JMenuItem("TWiki");
        subMenuItem.setSelected(true);
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (MindRaider.profile.getActiveNotebook() == null) {
                    JOptionPane.showMessageDialog(MindRaiderJFrame.this, Messages.getString("MindRaiderJFrame.exportNotebookWarning"), Messages.getString("MindRaiderJFrame.exportError"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JFileChooser fc = new JFileChooser();
                fc.setApproveButtonText(Messages.getString("MindRaiderJFrame.export"));
                fc.setControlButtonsAreShown(true);
                fc.setDialogTitle(Messages.getString("MindRaiderJFrame.chooseExportDirectory"));
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                String exportDirectory = MindRaider.profile.getHomeDirectory() + File.separator + "export" + File.separator + "twiki";
                Utils.createDirectory(exportDirectory);
                fc.setCurrentDirectory(new File(exportDirectory));
                int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final String dstFileName = fc.getSelectedFile().getAbsolutePath() + File.separator + "TWIKI-EXPORT-" + MindRaider.notebookCustodian.getActiveNotebookNcName() + ".txt";
                    logger.debug(Messages.getString("MindRaiderJFrame.exportingToFile", dstFileName));
                    MindRaider.notebookCustodian.exportNotebook(NotebookCustodian.FORMAT_TWIKI, dstFileName);
                } else {
                    logger.debug(Messages.getString("MindRaiderJFrame.exportCommandCancelledByUser"));
                }
            }
        });
        submenu.add(subMenuItem);
        subMenuItem = new JMenuItem("SKOS");
        subMenuItem.setSelected(true);
        subMenuItem.setEnabled(false);
        submenu.add(subMenuItem);
        menu.add(submenu);
        submenu = new JMenu(Messages.getString("MindRaiderJFrame.import"));
        submenu.setMnemonic(KeyEvent.VK_I);
        subMenuItem = new JMenuItem("TWiki");
        subMenuItem.setSelected(true);
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().clear();
                MindRaider.profile.setActiveNotebookUri(null);
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final File file = fc.getSelectedFile();
                    MindRaider.profile.deleteActiveModel();
                    logger.debug(Messages.getString("MindRaiderJFrame.importingTWikiTopic", file.getAbsolutePath()));
                    final SwingWorker worker = new SwingWorker() {

                        public Object construct() {
                            ProgressDialogJFrame progressDialogJFrame = new ProgressDialogJFrame(Messages.getString("MindRaiderJFrame.twikiImport"), Messages.getString("MindRaiderJFrame.processingTopicTWiki"));
                            try {
                                MindRaider.notebookCustodian.importNotebook(NotebookCustodian.FORMAT_TWIKI, (file != null ? file.getAbsolutePath() : null), progressDialogJFrame);
                            } finally {
                                if (progressDialogJFrame != null) {
                                    progressDialogJFrame.dispose();
                                }
                            }
                            return null;
                        }
                    };
                    worker.start();
                } else {
                    logger.debug(Messages.getString("MindRaiderJFrame.openCommandCancelledByUser"));
                }
            }
        });
        submenu.add(subMenuItem);
        subMenuItem = new JMenuItem("Google Notebook");
        subMenuItem.setEnabled(false);
        submenu.add(subMenuItem);
        menu.add(submenu);
        menu.addSeparator();
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.details"));
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new RdfModelDetailsJDialog();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu(Messages.getString("MindRaiderJFrame.concept"));
        menu.setMnemonic(KeyEvent.VK_C);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.new"));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptNew();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.open"));
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (MindRaider.profile.getActiveNotebookUri() != null) {
                    new OpenConceptJDialog();
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.discard"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptDiscard();
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.createConceptLink"));
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new ConceptInterlinkingJDialog();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.toggleEdit"));
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptJPanel.toggleAnnotationEditation();
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.up"));
        menuItem.setMnemonic(KeyEvent.VK_U);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptUp();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.promote"));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptPromote();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.demote"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptDemote();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.down"));
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptDown();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        if (!MindRaider.OUTLINER_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu = new JMenu(Messages.getString("MindRaiderJFrame.model"));
            menu.setMnemonic(KeyEvent.VK_O);
            menu.getAccessibleContext().setAccessibleDescription(Messages.getString("MindRaiderJFrame.rdfModel"));
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.new"), KeyEvent.VK_N);
            menuItem.setMnemonic(KeyEvent.VK_N);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    MindRaider.profile.setActiveNotebookUri(null);
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.deleteActiveModel();
                    spiders.clear();
                    showSpidersGraphOnly();
                    MindRaider.setModeRdf();
                    new NewRdfModelJDialog();
                }
            });
            menu.add(menuItem);
            menuItem = new JMenu(Messages.getString("MindRaiderJFrame.openFrom"));
            menuItem.setMnemonic(KeyEvent.VK_O);
            subMenuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.file"));
            subMenuItem.setMnemonic(KeyEvent.VK_F);
            subMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setSemanticWebUiLayout();
                    JFileChooser fc = new JFileChooser();
                    fc.setCurrentDirectory(new File(MindRaider.rdfCustodian.getModelsDirectory()));
                    fc.setDialogTitle(Messages.getString("MindRaiderJFrame.openModel"));
                    int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        MindRaider.profile.deleteActiveModel();
                        logger.debug(Messages.getString("MindRaiderJFrame.openingModel", file.getAbsolutePath()));
                        try {
                            spiders.load(file.getAbsolutePath());
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(MindRaiderJFrame.this, Messages.getString("MindRaiderJFrame.unableToLoadRDFModel", e1.getMessage()), Messages.getString("MindRaiderJFrame.loadModelError"), JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        StatusBar.show("Loaded model " + file.getAbsolutePath());
                    } else {
                        logger.debug(Messages.getString("MindRaiderJFrame.openCommandCancelledByUser"));
                    }
                }
            });
            menuItem.add(subMenuItem);
            subMenuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.url"));
            subMenuItem.setMnemonic(KeyEvent.VK_U);
            subMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.setActiveNotebookUri(null);
                    MindRaider.profile.deleteActiveModel();
                    spiders.clear();
                    showSpidersGraphOnly();
                    MindRaider.setModeRdf();
                    new DownloadModelJDialog(false);
                }
            });
            menuItem.add(subMenuItem);
            menu.add(menuItem);
            menuItem = new JMenu(Messages.getString("MindRaiderJFrame.addFrom"));
            menuItem.setMnemonic(KeyEvent.VK_O);
            subMenuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.file"));
            subMenuItem.setMnemonic(KeyEvent.VK_F);
            subMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.setActiveNotebookUri(null);
                    showSpidersGraphOnly();
                    MindRaider.setModeRdf();
                    JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        MindRaider.profile.deleteActiveModel();
                        logger.debug(Messages.getString("MindRaiderJFrame.openingModel", file.getAbsolutePath()));
                        spiders.addModel(file.getAbsolutePath());
                        StatusBar.show(Messages.getString("MindRaiderJFrame.addedModel", file.getAbsolutePath()));
                    } else {
                        logger.debug(Messages.getString("MindRaiderJFrame.openCommandCancelledByUser"));
                    }
                }
            });
            menuItem.add(subMenuItem);
            subMenuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.url"));
            subMenuItem.setMnemonic(KeyEvent.VK_U);
            subMenuItem.setMnemonic(KeyEvent.VK_U);
            subMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.setActiveNotebookUri(null);
                    showSpidersGraphOnly();
                    MindRaider.setModeRdf();
                    new DownloadModelJDialog(true);
                }
            });
            menuItem.add(subMenuItem);
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.save"));
            menuItem.setMnemonic(KeyEvent.VK_S);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (spiders.save()) {
                        StatusBar.show(Messages.getString("MindRaiderJFrame.modelSaved", spiders.getRdfModel()));
                    } else {
                        StatusBar.show(Messages.getString("MindRaiderJFrame.modelNotSaved"));
                    }
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.saveAs"));
            menuItem.setMnemonic(KeyEvent.VK_A);
            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String filename = null;
                    JFileChooser fc = new JFileChooser();
                    fc.setCurrentDirectory(new File(MindRaider.rdfCustodian.getModelsDirectory()));
                    fc.setDialogTitle(Messages.getString("MindRaiderJFrame.saveModelAs"));
                    int returnVal = fc.showSaveDialog(MindRaiderJFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        logger.debug(Messages.getString("MindRaiderJFrame.savingModel", file.getAbsolutePath()));
                        MindRaider.spidersGraph.saveAs(file.getAbsolutePath());
                    }
                    if (spiders.saveAs(filename)) {
                        StatusBar.show(Messages.getString("MindRaiderJFrame.modelSaved", spiders.getRdfModel()));
                    } else {
                        StatusBar.show(Messages.getString("MindRaiderJFrame.modelSaved"));
                    }
                }
            });
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.dump"));
            menuItem.setMnemonic(KeyEvent.VK_D);
            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    spiders.getRdfModel().show();
                }
            });
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.close"), KeyEvent.VK_C);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    NotebookOutlineJPanel.getInstance().clear();
                    spiders.clear();
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_C);
            menu.add(menuItem);
            menu.addSeparator();
            if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
                submenu = new JMenu(Messages.getString("MindRaiderJFrame.serialization"));
                submenu.setMnemonic(KeyEvent.VK_D);
                ButtonGroup serializationGroup = new ButtonGroup();
                rbMenuItem = new JRadioButtonMenuItem(Messages.getString("MindRaiderJFrame.rdfXML"));
                rbMenuItem.setSelected(true);
                rbMenuItem.setMnemonic(KeyEvent.VK_R);
                serializationGroup.add(rbMenuItem);
                submenu.add(rbMenuItem);
                rbMenuItem = new JRadioButtonMenuItem(Messages.getString("MindRaiderJFrame.siRS"));
                rbMenuItem.setSelected(true);
                rbMenuItem.setMnemonic(KeyEvent.VK_S);
                serializationGroup.add(rbMenuItem);
                submenu.add(rbMenuItem);
                rbMenuItem = new JRadioButtonMenuItem(Messages.getString("MindRaiderJFrame.triple"));
                rbMenuItem.setSelected(true);
                rbMenuItem.setMnemonic(KeyEvent.VK_T);
                serializationGroup.add(rbMenuItem);
                submenu.add(rbMenuItem);
                menu.add(submenu);
            }
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.details"));
            menuItem.setMnemonic(KeyEvent.VK_D);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new RdfModelDetailsJDialog();
                }
            });
            menu.add(menuItem);
            menuBar.add(menu);
        }
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu = new JMenu(Messages.getString("MindRaiderJFrame.mep"));
            menu.setMnemonic(KeyEvent.VK_E);
            menu.getAccessibleContext().setAccessibleDescription(Messages.getString("MindRaiderJFrame.mepDescription"));
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.install"), KeyEvent.VK_I);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_I);
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.configure"), KeyEvent.VK_C);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_C);
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.remove"), KeyEvent.VK_R);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_R);
            menu.add(menuItem);
            menuBar.add(menu);
        }
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu = new JMenu(Messages.getString("MindRaiderJFrame.p2p"));
            menu.setMnemonic(KeyEvent.VK_P);
            menu.getAccessibleContext().setAccessibleDescription(Messages.getString("MindRaiderJFrame.sharedMind"));
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.attachMind"), KeyEvent.VK_I);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_I);
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.synchronize"), KeyEvent.VK_C);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_C);
            menu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.configure"), KeyEvent.VK_R);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_R);
            menu.add(menuItem);
            menuBar.add(menu);
        }
        menu = new JMenu(Messages.getString("MindRaiderJFrame.tools"));
        menu.setMnemonic(KeyEvent.VK_T);
        menuItem = new JMenuItem("Robot Walker", KeyEvent.VK_R);
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.captureScreen"));
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setApproveButtonText(Messages.getString("MindRaiderJFrame.screenshot"));
                fc.setControlButtonsAreShown(true);
                fc.setDialogTitle(Messages.getString("MindRaiderJFrame.chooseScreenshotDirectory"));
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                String exportDirectory = MindRaider.profile.getHomeDirectory() + File.separator + "Screenshots";
                Utils.createDirectory(exportDirectory);
                fc.setCurrentDirectory(new File(exportDirectory));
                int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final String filename = fc.getSelectedFile().getAbsolutePath() + File.separator + "screenshot.jpg";
                    new Thread() {

                        public void run() {
                            OutputStream file = null;
                            try {
                                file = new FileOutputStream(filename);
                                Robot robot = new Robot();
                                robot.delay(1000);
                                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(file);
                                encoder.encode(robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
                            } catch (Exception e1) {
                                logger.error("Unable to capture screen!", e1);
                            } finally {
                                if (file != null) {
                                    try {
                                        file.close();
                                    } catch (IOException e1) {
                                        logger.error("Unable to close stream", e1);
                                    }
                                }
                            }
                        }
                    }.start();
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.backupRepository"));
        menuItem.setMnemonic(KeyEvent.VK_B);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Installer.backupRepositoryAsync();
            }
        });
        menu.add(menuItem);
        if (!MindRaider.OUTLINER_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu.addSeparator();
            submenu = new JMenu(Messages.getString("MindRaiderJFrame.uriqaBrowser"));
            submenu.setMnemonic(KeyEvent.VK_U);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.connectToServer"));
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    MindRaider.profile.setActiveNotebookUri(null);
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.deleteActiveModel();
                    spiders.clear();
                    showSpidersGraphOnly();
                    MindRaider.setModeUriqa();
                    new ConnectUriqaServerJDialog();
                }
            });
            submenu.add(menuItem);
            menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.disconnect"));
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            submenu.add(menuItem);
            menu.add(submenu);
        }
        menuBar.add(menu);
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu = new JMenu("Window");
            menu.setMnemonic(KeyEvent.VK_W);
            menuBar.add(menu);
        }
        menu = new JMenu(Messages.getString("MindRaiderJFrame.help"));
        menu.setMnemonic(KeyEvent.VK_H);
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.documentation"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    MindRaider.notebookCustodian.loadNotebook(new URI(MindRaiderVocabulary.getNotebookUri(NotebookCustodian.MR_DOC_NOTEBOOK_DOCUMENTATION_LOCAL_NAME)));
                    NotebookOutlineJPanel.getInstance().refresh();
                } catch (Exception e1) {
                    logger.error(Messages.getString("MindRaiderJFrame.unableToLoadHelp", e1.getMessage()));
                }
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Web Homepage");
        menuItem.setMnemonic(KeyEvent.VK_H);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Launcher.launchInBrowser("http://mindraider.sourceforge.net");
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Report Bug");
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Launcher.launchInBrowser("http://sourceforge.net/forum/?group_id=128454");
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Check for " + MindRaiderConstants.MR_TITLE + " Updates");
        menuItem.setMnemonic(KeyEvent.VK_F);
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem(Messages.getString("MindRaiderJFrame.about", MindRaiderConstants.MR_TITLE));
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new AboutJDialog();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
    }
