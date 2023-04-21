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
