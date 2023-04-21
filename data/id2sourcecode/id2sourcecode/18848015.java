                @Override
                public void run() {
                    String firstMessage = WaitingDialog.getFirstMessage();
                    boolean wasOpen = WaitingDialog.isInstanceVisible();
                    WaitingDialog.changeFirstMessage(Messages.getMessage("DIALOGS_FILE_WRITING"));
                    WaitingDialog.openInstance();
                    try {
                        FileWriter fw = new FileWriter(toSave);
                        Calendar calendar = Calendar.getInstance();
                        switch(spectrumType) {
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX:
                                fw.write("Time (s)\t");
                                for (int i = 0; i < indexLengthRead; i++) {
                                    if ((selectedRead == null) || (selectedRead.contains(i))) {
                                        fw.write(Integer.toString(i + 1));
                                        fw.write(" ");
                                        fw.write(Messages.getMessage("VIEW_SPECTRUM_READ"));
                                        fw.write("\t");
                                    }
                                }
                                for (int i = 0; i < indexLengthWrite; i++) {
                                    if ((selectedWrite == null) || (selectedWrite.contains(i))) {
                                        fw.write(Integer.toString(i + 1));
                                        fw.write(" ");
                                        fw.write(Messages.getMessage("VIEW_SPECTRUM_WRITE"));
                                        fw.write("\t");
                                    }
                                }
                                break;
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME:
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME_STACK:
                                fw.write("Index\t");
                                for (int i = 0; i < timeLengthRead; i++) {
                                    if ((selectedRead == null) || (selectedRead.contains(i))) {
                                        if (readData.getData_timed()[i].time == null) {
                                            fw.write("null");
                                        } else {
                                            calendar.setTimeInMillis(readData.getData_timed()[i].time);
                                            fw.write(DATE_FORMAT.format(calendar.getTime()));
                                        }
                                        fw.write(" ");
                                        fw.write(Messages.getMessage("VIEW_SPECTRUM_READ"));
                                        fw.write("\t");
                                    }
                                }
                                for (int i = 0; i < timeLengthWrite; i++) {
                                    if ((selectedWrite == null) || (selectedWrite.contains(i))) {
                                        if (writeData.getData_timed()[i].time == null) {
                                            fw.write("null");
                                        } else {
                                            calendar.setTimeInMillis(writeData.getData_timed()[i].time);
                                            fw.write(DATE_FORMAT.format(calendar.getTime()));
                                        }
                                        fw.write(" ");
                                        fw.write(Messages.getMessage("VIEW_SPECTRUM_WRITE"));
                                        fw.write("\t");
                                    }
                                }
                                break;
                        }
                        fw.write("\n");
                        fw.flush();
                        switch(spectrumType) {
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX:
                                int timeIndexRead = 0;
                                int timeIndexWrite = 0;
                                while ((timeIndexRead < timeLengthRead) || (timeIndexWrite < timeIndexWrite)) {
                                    boolean canWrite = true;
                                    long minTime;
                                    if ((timeIndexRead < timeLengthRead) && (timeIndexWrite < timeIndexWrite)) {
                                        if ((readData.getData_timed()[timeIndexRead].time == null) && (writeData.getData_timed()[timeIndexWrite].time == null)) {
                                            canWrite = false;
                                            timeIndexRead++;
                                            timeIndexWrite++;
                                        } else if (readData.getData_timed()[timeIndexRead].time == null) {
                                            appendSecondsToBufferFromMilliseconds(writeData.getData_timed()[timeIndexWrite].time, fw);
                                            fillIndexBufferWithNoValueString(fw, indexLengthRead, selectedRead);
                                            appendIndexesToBuffer(fw, writeData, timeIndexWrite, indexLengthWrite, selectedWrite);
                                            timeIndexRead++;
                                            timeIndexWrite++;
                                        } else if (writeData.getData_timed()[timeIndexWrite].time == null) {
                                            appendSecondsToBufferFromMilliseconds(readData.getData_timed()[timeIndexRead].time, fw);
                                            appendIndexesToBuffer(fw, readData, timeIndexRead, indexLengthRead, selectedRead);
                                            fillIndexBufferWithNoValueString(fw, indexLengthWrite, selectedWrite);
                                            timeIndexRead++;
                                            timeIndexWrite++;
                                        } else {
                                            minTime = Math.min(readData.getData_timed()[timeIndexRead].time.longValue(), writeData.getData_timed()[timeIndexWrite].time.longValue());
                                            appendSecondsToBufferFromMilliseconds(minTime, fw);
                                            if (minTime == readData.getData_timed()[timeIndexRead].time.longValue()) {
                                                appendIndexesToBuffer(fw, readData, timeIndexRead, indexLengthRead, selectedRead);
                                                timeIndexRead++;
                                                if (minTime == writeData.getData_timed()[timeIndexWrite].time.longValue()) {
                                                    appendIndexesToBuffer(fw, writeData, timeIndexWrite, indexLengthWrite, selectedWrite);
                                                    timeIndexWrite++;
                                                } else {
                                                    fillIndexBufferWithNoValueString(fw, indexLengthWrite, selectedWrite);
                                                }
                                            } else {
                                                fillIndexBufferWithNoValueString(fw, indexLengthRead, selectedRead);
                                                appendIndexesToBuffer(fw, writeData, timeIndexWrite, indexLengthWrite, selectedWrite);
                                                timeIndexWrite++;
                                            }
                                        }
                                    } else if (timeIndexRead < timeLengthRead) {
                                        appendSecondsToBufferFromMilliseconds(readData.getData_timed()[timeIndexRead].time, fw);
                                        appendIndexesToBuffer(fw, readData, timeIndexRead, indexLengthRead, selectedRead);
                                        fillIndexBufferWithNoValueString(fw, indexLengthWrite, selectedWrite);
                                        timeIndexRead++;
                                    } else if (timeIndexWrite < timeIndexWrite) {
                                        appendSecondsToBufferFromMilliseconds(writeData.getData_timed()[timeIndexWrite].time, fw);
                                        fillIndexBufferWithNoValueString(fw, indexLengthRead, selectedRead);
                                        appendIndexesToBuffer(fw, writeData, timeIndexWrite, indexLengthWrite, selectedWrite);
                                        timeIndexWrite++;
                                    }
                                    if (canWrite) {
                                        fw.write("\n");
                                        fw.flush();
                                    }
                                }
                                break;
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME:
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME_STACK:
                                int maxIndexLimit = Math.max(indexLengthRead, indexLengthWrite);
                                for (int index = 0; index < maxIndexLimit; index++) {
                                    new StringBuffer();
                                    fw.write(Double.toString(index));
                                    fw.write("\t");
                                    appendTimesToBuffer(fw, readData, timeLengthRead, index, selectedRead);
                                    appendTimesToBuffer(fw, writeData, timeLengthWrite, index, selectedWrite);
                                    fw.write("\n");
                                    fw.flush();
                                }
                                break;
                        }
                        fw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(caller, Messages.getMessage("DIALOGS_FILE_CHOOSER_FILE_SAVING_ERROR") + e.getMessage());
                    } catch (OutOfMemoryError oome) {
                        caller.outOfMemoryErrorManagement();
                    } finally {
                        if (WaitingDialog.isInstanceVisible()) {
                            if (wasOpen) {
                                WaitingDialog.changeFirstMessage(firstMessage);
                            } else {
                                WaitingDialog.closeInstance();
                            }
                        }
                    }
                }
