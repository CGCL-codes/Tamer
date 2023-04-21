    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.info) {
            new AboutBox(m);
            System.gc();
        } else if (e.getSource() == menu.gener || e.getSource() == m.toolBar.gener) m.generator.generate(false); else if (e.getSource() == menu.zippen || e.getSource() == m.toolBar.zippen) m.generator.generate(true); else if (e.getSource() == menu.gallerie || e.getSource() == m.toolBar.gallerie) new GallerieDialog(m); else if (e.getSource() == menu.exit) System.exit(0); else if (e.getSource() == menu.set_quality || e.getSource() == m.toolBar.preferences) m.openOptions(); else if (e.getSource() == menu.look_windows) {
            m.setLookFeel(winClassName);
            o.setLookAndFeel(winClassName);
        } else if (e.getSource() == menu.look_windows_classic) {
            m.setLookFeel(winClassicClassName);
            o.setLookAndFeel(winClassicClassName);
        } else if (e.getSource() == menu.look_nimbus) {
            m.setLookFeel(nimbusClassName);
            o.setLookAndFeel(nimbusClassName);
        } else if (e.getSource() == menu.look_metal) {
            m.setLookFeel(metalClassName);
            o.setLookAndFeel(metalClassName);
        } else if (e.getSource() == menu.look_motif) {
            m.setLookFeel(motifClassName);
            o.setLookAndFeel(motifClassName);
        } else if (e.getSource() == menu.look_gtk) {
            m.setLookFeel(gtkClassName);
            o.setLookAndFeel(gtkClassName);
        } else if (e.getSource() == menu.update_check) {
            try {
                URL url = new URL("http://www.jgeppert.com/jmjrst/jmjrst_version.txt");
                URLConnection uc = url.openConnection();
                uc.connect();
                InputStream is = uc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                line = br.readLine();
                br.close();
                isr.close();
                is.close();
                if (line != null) {
                    System.out.println(line);
                    if (!line.equalsIgnoreCase(m.mes.getString("Version"))) {
                        JLabel tf = new JLabel();
                        tf.setText(m.mes.getString("Messages.0"));
                        JOptionPane.showMessageDialog(m, tf, m.mes.getString("Menu.15"), JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JLabel tf = new JLabel();
                        tf.setText(m.mes.getString("Messages.1"));
                        JOptionPane.showMessageDialog(m, tf, m.mes.getString("Menu.15"), JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JLabel tf = new JLabel();
                    tf.setText(m.mes.getString("Messages.2"));
                    JOptionPane.showMessageDialog(m, tf, m.mes.getString("Menu.15"), JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JLabel tf = new JLabel();
                tf.setText(m.mes.getString("Messages.2"));
                JOptionPane.showMessageDialog(m, tf, m.mes.getString("Menu.15"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
