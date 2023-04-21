            @Override
            public void handleFile(File f) {
                if (!f.getName().endsWith(".jar")) {
                    JOptionPane.showMessageDialog(AvailableModulesPanel.this, "Impossible d'installer le module. Le fichier n'est pas un module.");
                    return;
                }
                File dir = new File("Modules");
                dir.mkdir();
                File out = null;
                if (dir.canWrite()) {
                    try {
                        out = new File(dir, f.getName());
                        FileUtils.copyFile(f, out);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(AvailableModulesPanel.this, "Impossible d'installer le module.\n" + f.getAbsolutePath() + " vers " + dir.getAbsolutePath());
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(AvailableModulesPanel.this, "Impossible d'installer le module.\nVous devez disposer des droits en écriture sur le dossier:\n" + dir.getAbsolutePath());
                    return;
                }
                try {
                    ModuleManager.getInstance().addFactory(new JarModuleFactory(out));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(AvailableModulesPanel.this, "Impossible d'intégrer le module.\n" + e.getMessage());
                    return;
                }
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        reload();
                    }
                });
            }
