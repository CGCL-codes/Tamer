    private void loadTable() {
        ArrayList<MqQueueManager> queueManagers = null;
        queueManagers = preferences.getQueueManagers();
        int size = queueManagers.size();
        TableModel model = new DefaultTableModel(columnNames, size);
        for (int i = 0; i < size; i++) {
            MqQueueManager qm = queueManagers.get(i);
            model.setValueAt(qm.getQManager(), i, 0);
            model.setValueAt(qm.getHostName(), i, 1);
            model.setValueAt(qm.getPort(), i, 2);
            model.setValueAt(qm.getChannel(), i, 3);
        }
        tblQMs.setModel(model);
        tblQMs.repaint();
    }
