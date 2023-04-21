            public void mouseReleased(final MouseEvent e) {
                int index = table.getSelectedRow();
                if (index != -1) {
                    if (rowSelectedAtMousePressed != index) {
                        Channel channel1 = tableModel.getChannels().get(index);
                        Channel channel2 = tableModel.getChannels().get(rowSelectedAtMousePressed);
                        String name = channel1.getName();
                        channel1.setName(channel2.getName());
                        channel2.setName(name);
                    }
                }
                rowSelectedAtMousePressed = -1;
            }
