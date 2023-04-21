    private void animateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        stopAnimation = false;
        final java.awt.event.ActionEvent ev = evt;
        Thread runner = new Thread() {

            @Override
            public void run() {
                boolean doLoop = false;
                boolean forthBack = false;
                boolean forth = true;
                if (ev.getSource() == fbLoopButton || ev.getSource() == loopButton) {
                    doLoop = true;
                    if (ev.getSource() == fbLoopButton) {
                        forthBack = true;
                    }
                }
                String selectedStr = "";
                int[] selectedInd = framesInt.getSelectedIndices();
                if (selectedInd.length == 0) {
                    javax.swing.JOptionPane.showMessageDialog(mainGUI, "You should select some frames.", "No frames selected", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for (int i = 0; i < selectedInd.length; ++i) {
                    selectedStr += framesInt.getDataIDForFrame(selectedInd[i]);
                    if (i < selectedInd.length - 1) {
                        selectedStr += ",";
                    }
                }
                try {
                    String selectStat = "select data.*,bodies.* from data,bodies where " + "data.id in (" + selectedStr + ") and data.id=bodies.dataid";
                    if (reverseAnimBox.isSelected()) {
                        selectStat += " order by data.id desc";
                    }
                    Statement mysqlStat = database.getMysqlSource().getConnection().createStatement();
                    ResultSet mysqlRes = mysqlStat.executeQuery(selectStat);
                    if (mysqlRes == null) {
                        javax.swing.JOptionPane.showMessageDialog(mainGUI, "animateButtonActionPerformed: No body data in database for frame " + framesInt.getCountForFrame(framesInt.getLeadSelectionIndex()) + ".", "No data", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } else {
                        database.getMysqlSource().getConnection().setAutoCommit(false);
                        mysqlRes.last();
                        int rowCount = mysqlRes.getRow();
                        mysqlRes.beforeFirst();
                        progrStat.setMinMaxWorkingVis(0, rowCount);
                        WriteBodyNameDBThread writerBodyName = new WriteBodyNameDBThread(database.getMysqlSource().getConnection(), mainGUI);
                        Thread writerBodyNameThread = new Thread(writerBodyName);
                        writerBodyNameThread.start();
                        FileWriter evalWriter = null;
                        String evalFileName = evalGUI.getFileName();
                        if (!evalFileName.isEmpty()) {
                            try {
                                evalWriter = new FileWriter(evalFileName);
                            } catch (IOException ex) {
                                javax.swing.JOptionPane.showMessageDialog(mainGUI, "animateButtonActionPerformed: Wasn't able to open file " + evalFileName + ".", "Open file error", javax.swing.JOptionPane.ERROR_MESSAGE);
                                evalWriter = null;
                            }
                        }
                        DataFrame data = null;
                        int count = -1, oldFrameCounter = -1, frameCounter, dataid = -1;
                        while (!stopAnimation) {
                            if ((forth && mysqlRes.first()) || (!forth && mysqlRes.last())) {
                                do {
                                    ++count;
                                    progrStat.setProgress(count);
                                    dataid = mysqlRes.getInt("data.id");
                                    frameCounter = mysqlRes.getInt("data.framecounter");
                                    if (oldFrameCounter != frameCounter) {
                                        if (data != null) {
                                            String refBodyString = optionsInt.getReferenceBody();
                                            int refBodyId = -1;
                                            TrackedBody refBody = null;
                                            if (refBodyString != null && !refBodyString.isEmpty() && !refBodyString.equals("none") && optionsInt.isUseForModelGen()) {
                                                refBodyId = Integer.parseInt(refBodyString);
                                                refBody = bodyIdMap.get(refBodyId);
                                            }
                                            if (evalGUI.isEvalActive()) {
                                                trackingData.getRefBodyTracking().fitModel(data, Double.parseDouble(sigmaTextField.getText()), Double.parseDouble(epsTextField.getText()), Double.valueOf(lprTextField.getText()), Double.valueOf(idpTextField.getText()), wtaNetBox.isSelected(), Double.parseDouble(minDistTextField.getText()), Integer.parseInt(maxStepsTextField.getText()), false, Double.parseDouble(resJointsRatio.getText()), inflDistPresCheckBox.isSelected(), inflAnglesResetCheckBox.isSelected(), refBody, optionsInt.isNotUseOther6D(), resetPosMarkerPosAfterCheckBox.isSelected(), winnerMethodComboBox.getSelectedIndex());
                                            }
                                            trackingData.getBodyTracking().fitModel(data, Double.parseDouble(sigmaTextField.getText()), Double.parseDouble(epsTextField.getText()), Double.valueOf(lprTextField.getText()), Double.valueOf(idpTextField.getText()), wtaNetBox.isSelected(), Double.parseDouble(minDistTextField.getText()), Integer.parseInt(maxStepsTextField.getText()), true, Double.parseDouble(resJointsRatio.getText()), inflDistPresCheckBox.isSelected(), inflAnglesResetCheckBox.isSelected(), refBody, optionsInt.isNotUseOther6D(), resetPosMarkerPosAfterCheckBox.isSelected(), winnerMethodComboBox.getSelectedIndex());
                                            if (evalGUI.isEvalActive()) {
                                                InitialModelBase.Eval eval = trackingData.getRefBodyModel().doEvaluation(trackingData.getBodyModel());
                                                int numNoNames = 0, numDoubleNames = 0;
                                                for (Iterator<TrackedBody> evalBodyIt = data.bodies.iterator(); evalBodyIt.hasNext(); ) {
                                                    TrackedBody bd = evalBodyIt.next();
                                                    if (bd.name.isEmpty()) {
                                                        ++numNoNames;
                                                    } else {
                                                        for (Iterator<TrackedBody> otherBodyIt = data.bodies.iterator(); otherBodyIt.hasNext(); ) {
                                                            TrackedBody obd = otherBodyIt.next();
                                                            if (bd != obd && obd.name.equals(bd.name)) {
                                                                ++numDoubleNames;
                                                            }
                                                        }
                                                    }
                                                }
                                                double noNames = (double) numNoNames / (double) data.bodies.size();
                                                double doubleNames = (double) numDoubleNames / (double) data.bodies.size();
                                                if (evalWriter != null) {
                                                    try {
                                                        evalWriter.write(data.internalTimeStamp + " " + eval.minError + " " + eval.avrgError + " " + eval.maxError + " " + eval.errorDeviation + " " + noNames + " " + doubleNames + "\n");
                                                    } catch (IOException ex) {
                                                        javax.swing.JOptionPane.showMessageDialog(mainGUI, "animateButtonActionPerformed: Wasn't able to write to evaluation file.", "Write error", javax.swing.JOptionPane.ERROR_MESSAGE);
                                                        evalWriter = null;
                                                    }
                                                } else {
                                                    System.out.println(data.internalTimeStamp + " " + eval.minError + " " + eval.avrgError + " " + eval.maxError + " " + eval.errorDeviation + " " + noNames + " " + doubleNames);
                                                }
                                            }
                                            gl.getEventListener().setTrackData(data);
                                            if (writeDBCheckBox.isSelected()) {
                                                writerBodyName.addToQueue(data, dataid);
                                            }
                                            setCurTrackData(data);
                                        }
                                        oldFrameCounter = frameCounter;
                                        data = new DataFrame();
                                        data.frameCounter = frameCounter;
                                        data.internalTimeStamp = mysqlRes.getLong("data.internaltimestamp");
                                        data.numCalibBodies = mysqlRes.getInt("data.calibBodies");
                                        data.seriesid = mysqlRes.getInt("data.seriesid");
                                        data.specialFlag = mysqlRes.getString("data.flag");
                                        data.timeStamp = mysqlRes.getDouble("data.timestamp");
                                    }
                                    TrackedBody body = new TrackedBody();
                                    body.dbid = mysqlRes.getInt("bodies.id");
                                    body.artid = mysqlRes.getInt("bodies.artid");
                                    body.name = mysqlRes.getString("bodies.name");
                                    body.quality = mysqlRes.getDouble("bodies.quality");
                                    body.button = mysqlRes.getInt("bodies.button");
                                    body.type = mysqlRes.getInt("bodies.type");
                                    body.trackedId = mysqlRes.getInt("bodies.trackedid");
                                    body.position[0] = mysqlRes.getDouble("bodies.pos1");
                                    body.position[1] = mysqlRes.getDouble("bodies.pos2");
                                    body.position[2] = mysqlRes.getDouble("bodies.pos3");
                                    body.angles[0] = mysqlRes.getDouble("bodies.angle1");
                                    body.angles[1] = mysqlRes.getDouble("bodies.angle2");
                                    body.angles[2] = mysqlRes.getDouble("bodies.angle3");
                                    body.matrix[0] = mysqlRes.getDouble("bodies.mat1");
                                    body.matrix[1] = mysqlRes.getDouble("bodies.mat2");
                                    body.matrix[2] = mysqlRes.getDouble("bodies.mat3");
                                    body.matrix[3] = mysqlRes.getDouble("bodies.mat4");
                                    body.matrix[4] = mysqlRes.getDouble("bodies.mat5");
                                    body.matrix[5] = mysqlRes.getDouble("bodies.mat6");
                                    body.matrix[6] = mysqlRes.getDouble("bodies.mat7");
                                    body.matrix[7] = mysqlRes.getDouble("bodies.mat8");
                                    body.matrix[8] = mysqlRes.getDouble("bodies.mat9");
                                    boolean isOccluded = false;
                                    if (evalGUI.isStaticOccl()) {
                                        double xmin, xmax, ymin, ymax, zmin, zmax;
                                        OccludedTrackingEvalRect[] areaItems = evalGUI.getAreaItems();
                                        for (int i = 0; i < areaItems.length && !isOccluded; ++i) {
                                            xmin = areaItems[i].xmin;
                                            xmax = areaItems[i].xmax;
                                            ymin = areaItems[i].ymin;
                                            ymax = areaItems[i].ymax;
                                            zmin = areaItems[i].zmin;
                                            zmax = areaItems[i].zmax;
                                            double temp;
                                            if (xmax < xmin) {
                                                temp = xmax;
                                                xmax = xmin;
                                                xmin = temp;
                                            }
                                            if (ymax < ymin) {
                                                temp = ymax;
                                                ymax = ymin;
                                                ymin = temp;
                                            }
                                            if (zmax < zmin) {
                                                temp = zmax;
                                                zmax = zmin;
                                                zmin = temp;
                                            }
                                            if (xmin <= body.position[0] && xmax >= body.position[0] && ymin <= body.position[1] && ymax >= body.position[1] && zmin <= body.position[2] && zmax >= body.position[2]) {
                                                isOccluded = true;
                                            }
                                        }
                                    }
                                    if (evalGUI.isRandomOccl()) {
                                        Vector<OccludedTrackingEvalRect> removeBoxes = new Vector<OccludedTrackingEvalRect>();
                                        for (Iterator<OccludedTrackingEvalRect> boxIt = occlAreas.iterator(); boxIt.hasNext(); ) {
                                            OccludedTrackingEvalRect occlBox = boxIt.next();
                                            if (data.internalTimeStamp - occlBox.timestamp > occlBox.lasting) {
                                                removeBoxes.add(occlBox);
                                            }
                                        }
                                        for (Iterator<OccludedTrackingEvalRect> boxIt = removeBoxes.iterator(); boxIt.hasNext(); ) {
                                            OccludedTrackingEvalRect occlBox = boxIt.next();
                                            occlAreas.remove(occlBox);
                                        }
                                        if (occlAreas.size() < evalGUI.getMinNrAreas() || occlAreas.size() > evalGUI.getMaxNrAreas()) {
                                            Random random = new Random();
                                            int addNr = (int) (random.nextDouble() * (double) (evalGUI.getMaxNrAreas() - evalGUI.getMinNrAreas()) + (double) evalGUI.getMinNrAreas());
                                            for (int i = 0; i < addNr; ++i) {
                                                double xmin = random.nextDouble() * 3000 - 1500, xmax = random.nextDouble() * 3000 - 1500, ymin = random.nextDouble() * 3000 - 1500, ymax = random.nextDouble() * 3000 - 1500, zmin = random.nextDouble() * 3000 - 1500, zmax = random.nextDouble() * 3000 - 1500;
                                                double temp;
                                                if (xmax < xmin) {
                                                    temp = xmax;
                                                    xmax = xmin;
                                                    xmin = temp;
                                                }
                                                if (ymax < ymin) {
                                                    temp = ymax;
                                                    ymax = ymin;
                                                    ymin = temp;
                                                }
                                                if (zmax < zmin) {
                                                    temp = zmax;
                                                    zmax = zmin;
                                                    zmin = temp;
                                                }
                                                OccludedTrackingEvalRect newBox = new OccludedTrackingEvalRect(xmin, xmax, ymin, ymax, zmin, zmax);
                                                newBox.timestamp = data.internalTimeStamp;
                                                newBox.lasting = (long) (random.nextDouble() * (double) (evalGUI.getMaxChangeTime() - evalGUI.getMinChangeTime()) + (double) evalGUI.getMinChangeTime());
                                                occlAreas.add(newBox);
                                            }
                                        }
                                        for (Iterator<OccludedTrackingEvalRect> boxIt = occlAreas.iterator(); boxIt.hasNext(); ) {
                                            OccludedTrackingEvalRect occlBox = boxIt.next();
                                            if (occlBox.xmin <= body.position[0] && occlBox.xmax >= body.position[0] && occlBox.ymin <= body.position[1] && occlBox.ymax >= body.position[1] && occlBox.zmin <= body.position[2] && occlBox.zmax >= body.position[2]) {
                                                isOccluded = true;
                                            }
                                        }
                                    }
                                    body.occluded = isOccluded;
                                    data.bodies.add(body);
                                    Thread.yield();
                                } while (!stopAnimation && ((forth && mysqlRes.next()) || (!forth && mysqlRes.previous())));
                                if (!doLoop) {
                                    stopAnimation = true;
                                }
                                if (forthBack) {
                                    forth = !forth;
                                    count = -1;
                                }
                            }
                        }
                        if (writeDBCheckBox.isSelected()) {
                            database.getMysqlSource().getConnection().commit();
                        }
                    }
                    database.getMysqlSource().getConnection().setAutoCommit(true);
                } catch (SQLException ex) {
                    javax.swing.JOptionPane.showMessageDialog(mainGUI, "animateButtonActionPerformed: Couldn't retrieve data: " + ex.getMessage(), "SQL error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                progrStat.resetIdleInvis();
            }
        };
        runner.start();
    }
