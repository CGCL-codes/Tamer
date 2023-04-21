    public void saveScreenshot(final HistoryNode node) {
        final FileFilter pngFilter = new FileFilter() {

            @Override
            public boolean accept(final File f) {
                if (f.isDirectory()) return true; else return f.getName().endsWith(".png");
            }

            @Override
            public String getDescription() {
                return "Saved Screenshots, *.png";
            }
        };
        final JFileChooser fileChooser = new SaveGameFileChooser();
        fileChooser.setFileFilter(pngFilter);
        final int rVal = fileChooser.showSaveDialog(this);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".png")) f = new File(f.getParent(), f.getName() + ".png");
            if (f.exists()) {
                final int choice = JOptionPane.showConfirmDialog(this, "A file by that name already exists. Do you wish to over write it?", "Over-write?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (choice != JOptionPane.OK_OPTION) return;
            }
            final File file = f;
            final Runnable t = new Runnable() {

                public void run() {
                    if (saveScreenshot(node, file)) JOptionPane.showMessageDialog(TripleAFrame.this, "Screenshot Saved", "Screenshot Saved", JOptionPane.INFORMATION_MESSAGE);
                }
            };
            if (!SwingUtilities.isEventDispatchThread()) {
                try {
                    SwingUtilities.invokeAndWait(t);
                } catch (final Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                t.run();
            }
        }
    }
