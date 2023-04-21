                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = Utils.initFileChooser();
                    int returnVal = fc.showDialog(SmileysButton.this, "Save");
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        boolean write = false;
                        if (fc.getSelectedFile().isFile()) {
                            if (JOptionPane.showConfirmDialog(null, "The file you selected already exists!\n" + "Do you want to overwrite?", "Overwrite", JOptionPane.YES_NO_OPTION) == 0) {
                                write = true;
                            }
                        } else {
                            write = true;
                        }
                        if (write) {
                            int w = ii.getIconWidth();
                            int h = ii.getIconHeight();
                            BufferedImage save = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2 = save.createGraphics();
                            ii.paintIcon(null, g2, 0, 0);
                            g2.dispose();
                            try {
                                ImageIO.write(save, "jpg", new File(fc.getSelectedFile().getCanonicalPath()));
                                JOptionPane.showMessageDialog(null, "Saved...");
                            } catch (IOException ioe) {
                                System.err.println("write: " + ioe.getMessage());
                                JOptionPane.showMessageDialog(null, "RUN TO THE WOODS!!!", "Saving failed", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
