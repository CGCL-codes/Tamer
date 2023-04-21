            public void actionPerformed(ActionEvent e) {
                if (!read.isSelected() && !write.isSelected()) {
                    ok.setEnabled(false);
                } else {
                    ok.setEnabled(true);
                }
            }
