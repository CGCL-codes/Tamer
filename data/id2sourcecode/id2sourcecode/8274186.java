                public void actionPerformed(ActionEvent e) {
                    final Crayon selectedCrayon = (Crayon) crayonList.getSelectedValue();
                    if (selectedCrayon == null) return;
                    final int crayonIndex = crayonList.getSelectedIndex();
                    if (templateManager.getDataGrid() != null) {
                        List<Strip> strips = templateManager.getDataGrid().getStripList();
                        for (Iterator<Strip> it = strips.iterator(); it.hasNext(); ) {
                            final Strip s = it.next();
                            InputChannelItemInterface channel = s.getChannel(crayonIndex);
                            if (channel != null) {
                                JOptionPane.showMessageDialog(templateManager, CHANNEL_IN_USE_MSG, CHANNEL_IN_USE_TITLE, JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }
                    for (int i = 0; i < repModel.getRowCount(); i++) {
                        repModel.getRow(i).delCrayon(selectedCrayon);
                    }
                    removeDataSeries(selectedCrayon);
                    crayonListModel.removeElement(selectedCrayon);
                    currentTemplate.removeCrayon(selectedCrayon);
                    repTable.repaint();
                }
