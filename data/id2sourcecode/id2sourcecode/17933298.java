            public void widgetSelected(SelectionEvent e) {
                Image image = pipelineTable.takeShot();
                FileDialog fd = new FileDialog(topShell, SWT.SAVE);
                fd.setFilterExtensions(new String[] { "*.png", "*.*" });
                String filename = fd.open();
                if (filename != null) {
                    boolean saveFile = true;
                    if (new File(filename).exists()) {
                        MessageBox box = new MessageBox(topShell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                        box.setMessage(String.format("File %s already exists, overwrite?", filename));
                        saveFile = (box.open() != SWT.CANCEL);
                    }
                    if (saveFile) {
                        ImageLoader loader = new ImageLoader();
                        ImageData imageData = image.getImageData();
                        loader.data = new ImageData[] { imageData };
                        try {
                            loader.save(filename, SWT.IMAGE_PNG);
                            console.logf("Pipeline screenshot saved to %s.\n", filename);
                        } catch (SWTException ex) {
                            if (ex.code == SWT.ERROR_IO) {
                                console.logf("Cannot save screenshot to %s, I/O problem (%s).\n", filename, ex.throwable.getMessage());
                            } else {
                                throw ex;
                            }
                        }
                    }
                }
                image.dispose();
            }
