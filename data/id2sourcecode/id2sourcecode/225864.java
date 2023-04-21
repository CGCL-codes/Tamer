            public void actionPerformed(ActionEvent ae) {
                String targetFile = Utils.selectFile("Save file as", fNode.getName(), FileObserver.this);
                try {
                    FileUtils.copyFile(fNode.getLocalURL(), new File(targetFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
