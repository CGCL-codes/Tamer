    private void genMovieButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            updateTime(startTime, startTimeField);
            updateTime(endTime, endTimeField);
            timeStep = Double.parseDouble(timeStepField.getText());
            if (((Integer) playBackRateSpinner.getValue()).intValue() < 1) {
                playBackRateSpinner.setValue(1);
            }
            playbackFPS = ((Integer) playBackRateSpinner.getValue()).intValue();
            updateDisplayData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "PARAMETER ERROR : " + e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String outputMoviePath = "test.mov";
        final JFileChooser fc = new JFileChooser();
        jsattrak.utilities.CustomFileFilter movFilter = new jsattrak.utilities.CustomFileFilter("mov", "*.mov");
        fc.addChoosableFileFilter(movFilter);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String fileExtension = "mov";
            if (fc.getFileFilter() == movFilter) {
                fileExtension = "mov";
            }
            String extension = getExtension(file);
            if (extension != null) {
                fileExtension = extension;
            } else {
                file = new File(file.getAbsolutePath() + "." + fileExtension);
            }
            outputMoviePath = "file:" + file.getAbsolutePath();
        } else {
            return;
        }
        boolean success = (new File(tempDirStr)).mkdir();
        double deltaTsec = (endTime.getMJD() - startTime.getMJD()) * 24 * 60 * 60.0;
        final int numFrames = (int) Math.ceil(deltaTsec / timeStep);
        final String outputMoviePathFinal = outputMoviePath;
        SwingWorker<Object, Integer> worker = new SwingWorker<Object, Integer>() {

            public Vector<String> doInBackground() {
                Vector<String> inputFiles = new Vector<String>();
                for (int i = 0; i < numFrames; i++) {
                    app.setTime(startTime.getCurrentGregorianCalendar().getTimeInMillis());
                    if (movieMode == 0) {
                        threeDpanel.getWwd().redrawNow();
                    } else if (movieMode == 1) {
                    } else {
                    }
                    createScreenCapture(tempDirStr + "/" + rootNameTempImages + i + ".jpg");
                    inputFiles.addElement(tempDirStr + "/" + rootNameTempImages + i + ".jpg");
                    publish((int) (100.0 * (i + 1.0) / numFrames));
                    startTime.addSeconds(timeStep);
                }
                movieStatusBar.setString("Building Movie File.....");
                movieStatusBar.setIndeterminate(true);
                int width;
                int height;
                if (movieMode == 0) {
                    width = threeDpanel.getWwdWidth();
                    height = threeDpanel.getWwdHeight();
                } else if (movieMode == 1) {
                    int[] twoDinfo = calculate2DMapSizeAndScreenLoc(twoDpanel);
                    width = twoDinfo[0];
                    height = twoDinfo[1];
                } else {
                    width = otherPanel.getWidth();
                    height = otherPanel.getHeight();
                }
                MediaLocator oml;
                if ((oml = createMediaLocator(outputMoviePathFinal)) == null) {
                    JOptionPane.showMessageDialog(null, "ERROR Creating Output File (check permissions)", "ERROR", JOptionPane.ERROR_MESSAGE);
                    System.err.println("Cannot build media locator from: " + outputMoviePathFinal);
                    publish(0);
                    movieStatusBar.setString("ERROR!");
                    return inputFiles;
                }
                JpegImagesToMovie imageToMovie = new JpegImagesToMovie();
                imageToMovie.doIt(width, height, playbackFPS, inputFiles, oml);
                boolean cleanSuccess = deleteDirectory(tempDirStr);
                publish(0);
                movieStatusBar.setString("Finished!");
                return inputFiles;
            }

            @Override
            protected void process(List<Integer> chunks) {
                int val = chunks.get(chunks.size() - 1);
                movieStatusBar.setValue(val);
                movieStatusBar.repaint();
            }

            @Override
            protected void done() {
                movieStatusBar.setIndeterminate(false);
                movieStatusBar.setValue(0);
                app.forceRepainting();
            }
        };
        worker.execute();
    }
