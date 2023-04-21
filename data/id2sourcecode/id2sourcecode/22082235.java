    public UserPanel(User baseUser) {
        final MessageDigest digester;
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            throw new RuntimeException(e1);
        }
        this.user = baseUser;
        this.workspace = (WabitWorkspace) this.user.getParent();
        this.loginTextField = new JTextField();
        this.loginTextField.setText(user.getName());
        this.loginLabel = new JLabel("User name");
        this.loginTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void textChanged(DocumentEvent e) {
                user.setName(loginTextField.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                textChanged(e);
            }

            public void insertUpdate(DocumentEvent e) {
                textChanged(e);
            }

            public void removeUpdate(DocumentEvent e) {
                textChanged(e);
            }
        });
        this.passwordTextField = new JPasswordField();
        this.passwordLabel = new JLabel("Password");
        this.passwordTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void textChanged(DocumentEvent e) {
                try {
                    String pass = new String(passwordTextField.getPassword());
                    String encoded = new String(Hex.encodeHex(digester.digest(pass.getBytes("UTF-8"))));
                    user.setPassword(encoded);
                } catch (UnsupportedEncodingException e1) {
                    throw new RuntimeException(e1);
                }
            }

            public void changedUpdate(DocumentEvent e) {
                textChanged(e);
            }

            public void insertUpdate(DocumentEvent e) {
                textChanged(e);
            }

            public void removeUpdate(DocumentEvent e) {
                textChanged(e);
            }
        });
        this.fullNameTextField = new JTextField();
        this.fullNameTextField.setText(user.getFullName());
        this.fullNameLabel = new JLabel("Full name");
        this.fullNameTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void textChanged(DocumentEvent e) {
                user.setFullName(fullNameTextField.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                textChanged(e);
            }

            public void insertUpdate(DocumentEvent e) {
                textChanged(e);
            }

            public void removeUpdate(DocumentEvent e) {
                textChanged(e);
            }
        });
        this.emailTextField = new JTextField();
        this.emailTextField.setText(user.getEmail());
        this.emailLabel = new JLabel("Email");
        this.emailTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void textChanged(DocumentEvent e) {
                user.setEmail(emailTextField.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                textChanged(e);
            }

            public void insertUpdate(DocumentEvent e) {
                textChanged(e);
            }

            public void removeUpdate(DocumentEvent e) {
                textChanged(e);
            }
        });
        this.availableGroupsLabel = new JLabel("Available Groups");
        this.availableGroupsListModel = new GroupsListModel(user, workspace, false);
        this.availableGroupsList = new JList(this.availableGroupsListModel);
        this.availableGroupsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.availableGroupsList.setCellRenderer(new DefaultListCellRenderer() {

            final JTree dummyTree = new JTree();

            final WorkspaceTreeCellRenderer delegate = new WorkspaceTreeCellRenderer();

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return delegate.getTreeCellRendererComponent(dummyTree, value, isSelected, false, true, 0, cellHasFocus);
            }
        });
        this.availableGroupsScrollPane = new JScrollPane(this.availableGroupsList);
        this.currentGroupsLabel = new JLabel("Current Memberships");
        this.currentGroupsListModel = new GroupsListModel(user, workspace, true);
        this.currentGroupsList = new JList(this.currentGroupsListModel);
        this.currentGroupsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.currentGroupsList.setCellRenderer(new DefaultListCellRenderer() {

            final JTree dummyTree = new JTree();

            final WorkspaceTreeCellRenderer delegate = new WorkspaceTreeCellRenderer();

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return delegate.getTreeCellRendererComponent(dummyTree, value, isSelected, false, true, 0, cellHasFocus);
            }
        });
        this.groupsLabel = new JLabel("Edit user memberships");
        this.currentGroupsScrollPane = new JScrollPane(this.currentGroupsList);
        this.addButton = new JButton(">");
        this.addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object[] selection = availableGroupsList.getSelectedValues();
                if (selection.length == 0) {
                    return;
                }
                try {
                    workspace.begin("Add user to groups");
                    for (Object object : selection) {
                        ((Group) object).addMember(new GroupMember(user));
                    }
                } finally {
                    workspace.commit();
                }
            }
        });
        this.removeButton = new JButton("<");
        this.removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object[] selection = currentGroupsList.getSelectedValues();
                if (selection.length == 0) {
                    return;
                }
                try {
                    workspace.begin("Remove user from groups");
                    Map<Group, GroupMember> toRemove = new ArrayMap<Group, GroupMember>();
                    for (Object object : selection) {
                        for (GroupMember membership : ((Group) object).getChildren(GroupMember.class)) {
                            if (membership.getUser().getUUID().equals(user.getUUID())) {
                                toRemove.put((Group) object, membership);
                            }
                        }
                    }
                    for (Entry<Group, GroupMember> entry : toRemove.entrySet()) {
                        entry.getKey().removeMember(entry.getValue());
                    }
                } finally {
                    workspace.commit();
                }
            }
        });
        Action deleteAction = new DeleteFromTreeAction(this.workspace, this.user, this.panel, this.workspace.getSession().getContext());
        this.toolbarBuilder.add(deleteAction, "Delete this user", WabitIcons.DELETE_ICON_32);
        JPanel namePassPanel = new JPanel(new MigLayout());
        namePassPanel.add(this.loginLabel, "align right, gaptop 20");
        namePassPanel.add(this.loginTextField, "span, wrap, wmin 600");
        namePassPanel.add(this.passwordLabel, "align right");
        namePassPanel.add(this.passwordTextField, "span, wrap, wmin 600");
        namePassPanel.add(this.fullNameLabel, "align right, gaptop 20");
        namePassPanel.add(this.fullNameTextField, "span, wrap, wmin 600");
        namePassPanel.add(this.emailLabel, "align right");
        namePassPanel.add(this.emailTextField, "span, wrap, wmin 600");
        this.panel.add(namePassPanel, "north");
        this.panel.add(this.groupsLabel, "span, wrap, gaptop 20, align center");
        JPanel buttonsPanel = new JPanel(new MigLayout());
        buttonsPanel.add(this.addButton, "wrap");
        buttonsPanel.add(this.removeButton);
        JPanel availablePanel = new JPanel(new MigLayout());
        availablePanel.add(this.availableGroupsLabel, "wrap, align center");
        availablePanel.add(this.availableGroupsScrollPane, "wmin 300");
        JPanel currentPanel = new JPanel(new MigLayout());
        currentPanel.add(this.currentGroupsLabel, "wrap, align center");
        currentPanel.add(this.currentGroupsScrollPane, "wmin 300");
        this.panel.add(availablePanel);
        this.panel.add(buttonsPanel, "shrink, span 1 2");
        this.panel.add(currentPanel);
    }
