    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        JMenu game = new JMenu("Game");
        final JMenu timer = new JMenu("Timer");
        JMenu help = new JMenu("Help");
        game.setMnemonic('G');
        timer.setMnemonic('T');
        help.setMnemonic('H');
        game.add(createMenuItem("New", 'N', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!isDone()) {
                    int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new game?", "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (a != JOptionPane.YES_OPTION) return;
                }
                t.cancel();
                createBoard();
            }
        }, "new", "Start a new game", KeyStroke.getKeyStroke("ctrl N")));
        game.add(createMenuItem("Load", 'L', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {

                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".su") || pathname.isDirectory();
                    }

                    public String getDescription() {
                        return "Sudoku Games (*.su)";
                    }
                };
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("Load Game");
                chooser.setApproveButtonText("Load");
                int returnVal = chooser.showOpenDialog(f);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    loadGame(chooser.getSelectedFile());
                }
            }
        }, "load", "Reload an old game from a file", KeyStroke.getKeyStroke("ctrl L")));
        game.add(createMenuItem("Save", 'S', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {

                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".su") || pathname.isDirectory();
                    }

                    public String getDescription() {
                        return "Sudoku Games (*.su)";
                    }
                };
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("Save Game");
                chooser.setApproveButtonText("Save");
                int returnVal = chooser.showSaveDialog(f);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    saveGame(chooser.getSelectedFile());
                }
            }
        }, "save", "Save this game to a file", KeyStroke.getKeyStroke("ctrl S")));
        game.add(createMenuItem("Reset", 'R', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!canUpdateTimer) {
                    JOptionPane.showMessageDialog(f, "The game cannot be reset while the \n" + "timer is stopped.", "Can't reset the game", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int a;
                if (((JMenuItem) (timer.getMenuComponent(2))).getText().equals("Hide")) a = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset this game?\n" + "The timer will not be reset.", "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); else a = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset this game?\n", "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (a != JOptionPane.YES_OPTION) return;
                for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) if (!original[r][c]) {
                    board.setIntAt(r, c, 0);
                    b[r][c].setText(" ");
                }
            }
        }, "reset", "Reset the game", KeyStroke.getKeyStroke("ctrl R")));
        game.addSeparator();
        game.add(createMenuItem("Print", 'P', new PrintListener(), "print", "Print the board", KeyStroke.getKeyStroke("ctrl P")));
        game.add(createMenuItem("Exit", 'x', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, "exit", "Quit Sudoku", KeyStroke.getKeyStroke("ctrl C")));
        timer.add(createMenuItem("Pause", 'P', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (stop.getText().equals("Pause")) {
                    stop.setText("Start");
                    for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {
                        backup[r][c] = b[r][c].getText();
                        b[r][c].setText(" ");
                    }
                    canUpdateTimer = false;
                } else {
                    return;
                }
            }
        }, "pause", "Pause the timer"));
        timer.add(createMenuItem("Start", 'S', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (stop.getText().equals("Pause")) {
                    return;
                } else {
                    for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {
                        b[r][c].setText(backup[r][c]);
                    }
                    stop.setText("Pause");
                    canUpdateTimer = true;
                }
            }
        }, "start", "Start the timer"));
        timer.add(createMenuItem("Hide", 'H', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (((JMenuItem) (timer.getMenuComponent(2))).getText().equals("Hide")) {
                    timerLabel.setVisible(false);
                    stop.setVisible(false);
                    f.pack();
                    f.adjustLocation();
                    ((JMenuItem) (timer.getMenuComponent(2))).setText("Show");
                    ((JMenuItem) (timer.getMenuComponent(2))).setMnemonic('h');
                    ((JMenuItem) (timer.getMenuComponent(2))).setToolTipText("Display " + "the timer");
                    timerIsUsed = false;
                } else {
                    timerLabel.setVisible(true);
                    stop.setVisible(true);
                    f.pack();
                    f.adjustLocation();
                    ((JMenuItem) (timer.getMenuComponent(2))).setText("Hide");
                    ((JMenuItem) (timer.getMenuComponent(2))).setMnemonic('H');
                    ((JMenuItem) (timer.getMenuComponent(2))).setToolTipText("Hide " + "the timer");
                    timerIsUsed = true;
                }
            }
        }, "hide", "Hide the timer", KeyStroke.getKeyStroke("ctrl H")));
        timer.add(createMenuItem("Best Times", 'B', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BestTimes.read().displayDialog(f);
            }
        }, "best", "View the best times", KeyStroke.getKeyStroke("ctrl B")));
        help.add(createMenuItem("Playing Sudoku", 'P', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(f, "The goal of sudoku is to fill in every square " + "with a number between 1 and 9 \nas long as no row, column, or box has two " + "squares with the same number.  " + "\n\nClick on a square and drag up or down to select a " + "number.  Release the mouse \nbutton to choose that number.", "Sudoku Help", JOptionPane.INFORMATION_MESSAGE);
            }
        }, "help", "Get help playing Sudoku"));
        help.add(createMenuItem("About", 'A', new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(f, "Sudoku Version " + versionString + "\n\n" + "Copyleft (L) 2008 Scott Lawrence\nLicensed under the " + "GNU GPL V3.", "About Sudoku", JOptionPane.INFORMATION_MESSAGE);
            }
        }, "about", "About Scott Lawrence's Sudoku"));
        menuBar.add(game);
        menuBar.add(timer);
        menuBar.add(help);
        setJMenuBar(menuBar);
    }
