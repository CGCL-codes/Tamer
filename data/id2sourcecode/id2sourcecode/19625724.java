    public void run() {
        df.resetImage();
        boolean lastCaptureWasDifferences = usingDifferences;
        while (sharerState == ACTIVE_SHARER) {
            if (sendingImages) {
                try {
                    AppSharePacket packet = null;
                    BufferedImage buff = robot.createScreenCapture(sharingRegion);
                    if (usingDifferences) {
                        if (!lastCaptureWasDifferences) {
                            df.resetImage();
                            lastCaptureWasDifferences = true;
                        }
                        Vector differences = df.createDifference(buff);
                        if (differences.size() > 0) packet = new AppSharePacket(differences, myClientKey);
                    } else {
                        lastCaptureWasDifferences = false;
                        if (!isDifferent(buff)) {
                            buff = null;
                        }
                        packet = new AppSharePacket(buff, myClientKey, quality);
                    }
                    if (packet != null) {
                        sendDataToOthers(packet);
                        if (previewing) {
                            if (appSharePanel != null) {
                                appSharePanel.setDisplayImageTitle("Previewing... sent " + packet.length + " bytes in image");
                                appSharePanel.displayImage(packet);
                                appSharePanel.imageLabel.setText(null);
                            }
                        } else {
                            if (appSharePanel != null) {
                                appSharePanel.setDisplayImageTitle("not Previewing...");
                                appSharePanel.displayImage(null);
                                appSharePanel.imageLabel.setText("sent " + packet.length + " bytes in image");
                            }
                        }
                        if (!controllingClient.equals("")) {
                            if (SessionUtilities.getVerbose()) System.out.println("Waiting for ACK from controller...");
                            waitForControllerToAck();
                            if (SessionUtilities.getVerbose()) System.out.println("Received ACK");
                        }
                    }
                } catch (java.awt.image.RasterFormatException rfe) {
                }
            }
            try {
                Thread.sleep(delayTime);
            } catch (Exception e) {
            }
        }
    }
