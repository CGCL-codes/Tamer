    @Override
    public void run() {
        Component parent = null;
        Object o = getValue(AbstractTreeAction.PARENTKEY);
        if ((o != null) && (o instanceof Component)) parent = (Component) o; else parent = null;
        IDecisionMethodsList methods = ((DecisionMethodsDataModule) module).getMethods();
        for (int i = 0; i < methods.size(); i++) {
            VerifyUnreachableRulesAction a = new VerifyUnreachableRulesAction(methods.getMethod(i));
            a.actionPerformed(null);
        }
        File file = ToxTreeActions.selectFile(parent, MolFileFilter.toxForest_ext, MolFileFilter.toxForest_ext_descr, false);
        if (file == null) return;
        try {
            if (file.exists() && (JOptionPane.showConfirmDialog(parent, "File " + file.getAbsolutePath() + " already exists.\nOverwrite?", "Please confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)) return;
            if (file.getAbsolutePath().toLowerCase().endsWith(".fml")) {
                FileOutputStream os = new FileOutputStream(file);
                Thread.currentThread().setContextClassLoader(Introspection.getLoader());
                XMLEncoder encoder = new XMLEncoder(os);
                encoder.writeObject(methods);
                encoder.close();
            } else {
                if (!file.getName().endsWith(MolFileFilter.toxForest_ext[1])) file = new File(file.getAbsolutePath() + MolFileFilter.toxForest_ext[1]);
                ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
                stream.writeObject(methods);
                stream.close();
            }
        } catch (IOException x) {
            JOptionPane.showMessageDialog(parent, "Error on saving rules", x.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
