    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().equals(JMenuItem.class)) {
            text = ((JMenuItem) e.getSource()).getText();
            if (text.equals("Exit")) {
                if (Main.configFile.trayIcon > 0) System.exit(0); else this.dispose();
            } else if (text.equals("Exit All")) {
                System.exit(0);
            } else if (text.equals("Open Graph")) {
                Main.showGraph();
            } else if (text.equals("Save As...")) {
                JOptionPane.showMessageDialog(this, "Some symbols may not be displayed correctly in the text file.", "Warning", JOptionPane.WARNING_MESSAGE);
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    String file = chooser.getSelectedFile().getPath();
                    if (file.lastIndexOf(".") < 0) file += ".txt";
                    if (file.substring(file.lastIndexOf(".") + 1).equals("txt")) {
                        File f = new File(file);
                        if (!f.exists() || JOptionPane.showConfirmDialog(this, "There is already a file with the name " + file.substring(file.lastIndexOf("\\") + 1) + ", are you sure you wish to overwrite it?") == JOptionPane.YES_OPTION) {
                            try {
                                PrintWriter out = new PrintWriter(new FileWriter(f));
                                String[] temp = output.getText().split("\n");
                                for (int i = 0; i < temp.length; i++) {
                                    out.write(Main.calculator.formatOutputASCII(temp[i]));
                                    out.println();
                                }
                                out.close();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            } else if (text.equals("About BeastCalc")) {
                Main.showAbout();
            } else if (text.equals("Help Contents")) {
                Main.showHelp();
            } else if (text.equals("Settings")) {
                Main.showSettings();
            }
        }
    }
