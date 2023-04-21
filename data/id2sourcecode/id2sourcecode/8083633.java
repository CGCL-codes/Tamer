    private ListModel threadsAsListModel() {
        DefaultListModel dlm = new DefaultListModel();
        ThreadGroup tg = rootThreadGroup();
        writeThreadGroupInfo(0, tg, dlm);
        return dlm;
    }
