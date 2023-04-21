        @Override
        public void actionPerformed(ActionEvent e) {
            String file;
            if (!(file = ExplorationWindow("Save", JFileChooser.DIRECTORIES_ONLY)).equals("")) {
                if (!m_model.SaveCastadiva(file)) {
                    ShowSaveErrorMessage("Directory not created! check your user read/write permission", "Save error...");
                }
            }
        }
