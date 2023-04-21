            public void actionPerformed(ActionEvent event) {
                String currentDirectory = _wireFileTracker.getRecentFolderPath();
                JFrame frame = new JFrame();
                JFileChooser fileChooser = new JFileChooser(currentDirectory);
                fileChooser.addChoosableFileFilter(new WireFileFilter());
                int status = fileChooser.showOpenDialog(frame);
                if (status == JFileChooser.APPROVE_OPTION) {
                    _wireFileTracker.cacheURL(fileChooser.getSelectedFile());
                    File file = fileChooser.getSelectedFile();
                    setWSFile(file);
                }
                mxProxy.setChannelSource(ModelProxy.PARAMSRC_DESIGN);
                MPXMain.PARAM_SRC = mxProxy.getChannelSource();
                syncModelAction.setEnabled(true);
                usePVLog = true;
            }
