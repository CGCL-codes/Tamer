    private synchronized void track(EventPacket<? extends BasicEvent> ae) {
        if (state == State.INITIAL) {
            if (initMask == null) {
                initMask = new boolean[chip.getSizeX()][chip.getSizeY()];
                for (int i = 0; i < chip.getSizeX(); i++) {
                    for (int j = 0; j < chip.getSizeY(); j++) initMask[i][j] = false;
                }
            }
            if (eventCounter >= eventsToInit) {
                kMeans();
                findFly();
                initWingEdges();
                state = nextState;
                eventCounter = 0;
            } else {
                for (int n = 0; n < ae.getSize(); n++) if (!initMask[ae.getEvent(n).x][ae.getEvent(n).y]) {
                    initPoints.add(new EventWingPair(ae.getEvent(n), WingType.Unknown));
                    initMask[ae.getEvent(n).x][ae.getEvent(n).y] = true;
                }
            }
            eventCounter += ae.getSize();
        }
        String logLine = new String();
        if (state == State.TRACKING) {
            Point2D.Float eventPoint = new Point2D.Float();
            float m1 = 1 - mixingFactor;
            float rads = 0f;
            for (BasicEvent o : ae) {
                PolarityEvent ev = (PolarityEvent) o;
                eventPoint.x = ev.x;
                eventPoint.y = ev.y;
                if (body.distance(ev.x, ev.y) > searchRange + searchRangeOffset) continue;
                rads = radiansInHeadingCircle(eventPoint);
                if (rads < Math.PI) {
                    leftBuffer.x += ev.getX();
                    leftBuffer.y += ev.getY();
                    leftBufferCount++;
                    if (ev.polarity == Off) {
                        leftLeadingEdge = m1 * leftLeadingEdge + mixingFactor * rads;
                        if (doLog) {
                            logLine = "1\t" + ev.getTimestamp() + "\t" + leftLeadingEdge + "\t" + frequenceLeft + "\t" + amplitudeLeft;
                        }
                    } else {
                        leftTrailingEdge = m1 * leftTrailingEdge + mixingFactor * rads;
                        if (doLog) {
                            logLine = "2\t" + ev.getTimestamp() + "\t" + leftTrailingEdge + "\t" + frequenceLeft + "\t" + amplitudeLeft;
                            ;
                        }
                    }
                    positionLeft = (leftLeadingEdge + leftTrailingEdge) / 2;
                } else {
                    rightBuffer.x += ev.getX();
                    rightBuffer.y += ev.getY();
                    rightBufferCount++;
                    if (ev.polarity == Off) {
                        rightLeadingEdge = m1 * rightLeadingEdge + mixingFactor * rads;
                        if (doLog) {
                            logLine = "3\t" + ev.getTimestamp() + "\t" + rightLeadingEdge + "\t" + frequenceRight + "\t" + amplitudeRight;
                            ;
                        }
                    } else {
                        rightTrailingEdge = m1 * rightTrailingEdge + mixingFactor * rads;
                        if (doLog) {
                            logLine = "4\t" + ev.getTimestamp() + "\t" + rightTrailingEdge;
                        }
                    }
                    positionRight = (rightLeadingEdge + rightTrailingEdge) / 2;
                }
                if (doLog) logLine = logLine + "\t" + rads;
                updateParams(ev.timestamp);
            }
        }
        if (state == State.KALMAN) {
            Point2D.Float eventPoint = new Point2D.Float();
            float rads = 0f;
            for (BasicEvent o : ae) {
                PolarityEvent ev = (PolarityEvent) o;
                eventPoint.x = ev.x;
                eventPoint.y = ev.y;
                if (body.distance(ev.x, ev.y) > searchRange + searchRangeOffset) continue;
                rads = radiansInHeadingCircle(eventPoint);
                if (rads < Math.PI) {
                    if (ev.polarity == Off) {
                        if (LLE == null) LLE = new EKF("left leading edge", ev.timestamp, centroidLeft);
                        if (!LLE.getUseEdge()) continue;
                        lleEventCount++;
                        lleBuffer += rads;
                        if (lleEventCount < nbEventsToCollectPerEdge) {
                            continue;
                        }
                        rads = lleBuffer / nbEventsToCollectPerEdge;
                        lleBuffer = 0f;
                        lleEventCount = 0;
                        LLE.predict(ev.timestamp);
                        LLE.update(rads);
                        if (doLog) {
                            logLine = "1\t" + ev.getTimestamp() + "\t" + LLE.getEdgeInRads() + "\t" + LLE.x[0] + "\t" + LLE.x[1] + "\t" + LLE.x[2] + "\t" + LLE.x[3] + "\t" + rads;
                        }
                    } else {
                        if (LTE == null) LTE = new EKF("left trailing edge", ev.timestamp, centroidLeft);
                        if (!LTE.getUseEdge()) continue;
                        lteEventCount++;
                        lteBuffer += rads;
                        if (lteEventCount < nbEventsToCollectPerEdge) {
                            continue;
                        }
                        rads = lteBuffer / nbEventsToCollectPerEdge;
                        lteBuffer = 0f;
                        lteEventCount = 0;
                        leftTrailingEdge = (1 - mixingFactor) * leftTrailingEdge + mixingFactor * rads;
                        positionLeft = (leftLeadingEdge + leftTrailingEdge) / 2;
                        updateParams(ev.timestamp);
                        LTE.predict(ev.timestamp);
                        LTE.update(rads);
                        if (doLog) {
                            logLine = "2\t" + ev.getTimestamp() + "\t" + LTE.getEdgeInRads() + "\t" + LTE.x[0] + "\t" + LTE.x[1] + "\t" + LTE.x[2] + "\t" + LTE.x[3] + "\t" + rads;
                        }
                    }
                } else {
                    if (ev.polarity == Off) {
                        if (RLE == null) RLE = new EKF("right leading edge", ev.timestamp, centroidRight);
                        if (!RLE.getUseEdge()) continue;
                        rleEventCount++;
                        rleBuffer += rads;
                        if (rleEventCount < nbEventsToCollectPerEdge) {
                            continue;
                        }
                        rads = rleBuffer / nbEventsToCollectPerEdge;
                        rleBuffer = 0f;
                        rleEventCount = 0;
                        RLE.predict(ev.timestamp);
                        RLE.update(rads);
                        if (doLog) {
                            logLine = "3\t" + ev.getTimestamp() + "\t" + RLE.getEdgeInRads() + "\t" + RLE.x[0] + "\t" + RLE.x[1] + "\t" + RLE.x[2] + "\t" + RLE.x[3] + "\t" + rads;
                        }
                    } else {
                        if (RTE == null) RTE = new EKF("right trailing edge", ev.timestamp, centroidRight);
                        if (!RTE.getUseEdge()) continue;
                        rteEventCount++;
                        rteBuffer += rads;
                        if (rteEventCount < nbEventsToCollectPerEdge) {
                            continue;
                        }
                        rads = rteBuffer / nbEventsToCollectPerEdge;
                        rteBuffer = 0f;
                        rteEventCount = 0;
                        RTE.predict(ev.timestamp);
                        RTE.update(rads);
                        if (doLog) {
                            logLine = "4\t" + ev.getTimestamp() + "\t" + RTE.getEdgeInRads() + "\t" + RTE.x[0] + "\t" + RTE.x[1] + "\t" + RTE.x[2] + "\t" + RTE.x[3] + "\t" + rads;
                        }
                    }
                }
            }
        }
        if (doLog) {
            try {
                if (logWriter != null) {
                    logWriter.write(logLine + nl);
                }
            } catch (IOException ioe) {
                System.out.println(ioe.toString());
            }
        }
    }
