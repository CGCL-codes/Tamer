    private void createContactWidget() {
        Connection connection = new Connection("jabber.org");
        ContactList contactList = connection.getContactList();
        if (connection.connect()) {
            if (connection.login("safroe", "fa234sdk", null)) {
                Roster roster = connection.getXmppConnection().getRoster();
                ArrayList<String> groupList = contactList.getGroupList();
                QTreeWidget treeWidget = new QTreeWidget(this);
                treeWidget.setColumnCount(1);
                treeWidget.setHeaderLabel("Kontakte");
                for (int i = 0; i < groupList.size(); i++) {
                    QTreeWidgetItem groupItem = new QTreeWidgetItem();
                    String groupName = groupList.get(i);
                    groupItem.setText(0, groupName);
                    RosterGroup group = roster.getGroup(groupName);
                    Iterator<RosterEntry> entries = group.getEntries().iterator();
                    while (entries.hasNext()) {
                        RosterEntry entry = entries.next();
                        QTreeWidgetItem entryItem = new QTreeWidgetItem(groupItem);
                        entryItem.setText(0, entry.getName());
                    }
                    treeWidget.addTopLevelItem(groupItem);
                }
                treeWidget.expandAll();
                treeWidget.addAction(null);
                treeWidget.clicked.connect(this, "newChat()");
                setCentralWidget(treeWidget);
            }
        }
    }
