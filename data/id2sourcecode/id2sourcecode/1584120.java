    private void showTaskList() {
        frame = new JFrame(Res.getString("title.tasks"));
        frame.setIconImage(SparkManager.getMainWindow().getIconImage());
        panel_events.removeAll();
        mainPanel.removeAll();
        mainPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, true, false));
        mainPanel.setBackground(Color.white);
        final JPanel topPanel = new JPanel(new GridBagLayout());
        final JTextField taskField = new JTextField();
        final JTextField dueDateField = new JTextField();
        final JButton addButton = new JButton(Res.getString("add"));
        final JLabel addTaskLabel = new JLabel(Res.getString("label.add.task"));
        topPanel.setOpaque(false);
        topPanel.add(addTaskLabel, new GridBagConstraints(0, 0, 1, 1, .9, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        topPanel.add(taskField, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
        topPanel.add(dueDateField, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 50, 0));
        topPanel.add(addButton, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        topPanel.add(new JLabel(Res.getString("label.timeformat", formatter.toPattern())), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        mainPanel.add(topPanel);
        final JPanel middlePanel = new JPanel(new GridBagLayout());
        final JLabel showLabel = new JLabel(Res.getString("label.show"));
        final JToggleButton allButton = new JToggleButton(Res.getString("button.tasks.all"));
        final JToggleButton activeButton = new JToggleButton(Res.getString("button.tasks.active"));
        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(allButton);
        buttonGroup.add(activeButton);
        middlePanel.setOpaque(false);
        middlePanel.add(showLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        middlePanel.add(allButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        middlePanel.add(activeButton, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        mainPanel.add(middlePanel);
        mainPanel.setBackground(Color.white);
        final JPanel titlePanel = new JPanel(new BorderLayout()) {

            private static final long serialVersionUID = -8812868562658925280L;

            public void paintComponent(Graphics g) {
                Color startColor = Color.white;
                Color endColor = new Color(198, 211, 247);
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, startColor, w, h, endColor, true);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, w, h);
            }
        };
        final JLabel taskLabel = new JLabel(Res.getString("label.due") + "        ");
        taskLabel.setFont(taskLabel.getFont().deriveFont(Font.BOLD));
        titlePanel.add(taskLabel, BorderLayout.EAST);
        mainPanel.add(titlePanel);
        Action showAllAction = new AbstractAction() {

            private static final long serialVersionUID = -7031122285194582204L;

            public void actionPerformed(ActionEvent e) {
                for (TaskUI ui : taskList) {
                    ui.setVisible(true);
                }
                SHOW_ALL_TASKS = true;
            }
        };
        Action showActiveAction = new AbstractAction() {

            private static final long serialVersionUID = -7551153291479117311L;

            public void actionPerformed(ActionEvent e) {
                for (TaskUI ui : taskList) {
                    if (ui.isSelected()) {
                        ui.setVisible(false);
                    }
                }
                SHOW_ALL_TASKS = false;
            }
        };
        final Action addAction = new AbstractAction() {

            private static final long serialVersionUID = -5937301529216080813L;

            public void actionPerformed(ActionEvent e) {
                String taskTitle = taskField.getText();
                if (!ModelUtil.hasLength(taskTitle)) {
                    return;
                }
                Task task = new Task();
                task.setTitle(taskTitle);
                final Date creationDate = new Date();
                task.setCreatedDate(creationDate.getTime());
                String dueDate = dueDateField.getText();
                if (ModelUtil.hasLength(dueDate)) {
                    try {
                        Date date = formatter.parse(dueDate);
                        task.setDueDate(date.getTime());
                    } catch (ParseException e1) {
                    }
                }
                taskField.setText("");
                final TaskUI taskUI = new TaskUI(task);
                panel_events.add(taskUI);
                taskList.add(taskUI);
                panel_events.invalidate();
                panel_events.validate();
                panel_events.repaint();
                mainPanel.invalidate();
                mainPanel.validate();
                mainPanel.repaint();
                frame.invalidate();
                frame.validate();
                frame.repaint();
            }
        };
        mainPanel.add(panel_events);
        panel_events.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, true, false));
        panel_events.setBackground(Color.white);
        allButton.addActionListener(showAllAction);
        activeButton.addActionListener(showActiveAction);
        GraphicUtils.makeSameSize(allButton, activeButton);
        addButton.addActionListener(addAction);
        Tasks tasks = Tasks.getTaskList(SparkManager.getConnection());
        updateTaskUI(tasks);
        if (SHOW_ALL_TASKS) {
            allButton.setSelected(true);
        } else {
            activeButton.setSelected(true);
            showActiveAction.actionPerformed(null);
        }
        long tomorrow = DateUtils.addDays(new Date().getTime(), 1);
        SimpleDateFormat formatter = new SimpleDateFormat(dateShortFormat);
        dueDateField.setText(formatter.format(new Date(tomorrow)));
        final JScrollPane pane = new JScrollPane(mainPanel);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(pane, BorderLayout.CENTER);
        frame.pack();
        frame.setSize(400, 400);
        final Action saveAction = new AbstractAction() {

            private static final long serialVersionUID = -4287799161421970177L;

            public void actionPerformed(ActionEvent actionEvent) {
                Tasks tasks = new Tasks();
                for (TaskUI ui : taskList) {
                    Task task = ui.getTask();
                    tasks.addTask(task);
                }
                Tasks.saveTasks(tasks, SparkManager.getConnection());
            }
        };
        addButton.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                    saveAction.actionPerformed(null);
                }
            }
        });
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowEvent) {
                saveAction.actionPerformed(null);
            }
        });
        taskField.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    addAction.actionPerformed(null);
                }
            }
        });
        GraphicUtils.centerWindowOnComponent(frame, SparkManager.getMainWindow());
        frame.setVisible(true);
    }
