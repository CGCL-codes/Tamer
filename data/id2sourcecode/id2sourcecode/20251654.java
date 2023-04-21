            @SuppressWarnings("unchecked")
            public void run(final IProgressMonitor monitor) {
                _isCancelImport = false;
                final Thread currentThread = Thread.currentThread();
                final Hashtable<String, Comparable<?>> environment = new Hashtable<String, Comparable<?>>();
                environment.put(GPSSerialDevice.PORT_NAME_KEY, portName);
                environment.put(GPSSerialDevice.PORT_SPEED_KEY, 9600);
                final Thread cancelObserver = new Thread(new Runnable() {

                    public void run() {
                        while (!monitor.isCanceled()) {
                            try {
                                Thread.sleep(100);
                            } catch (final InterruptedException ex) {
                                return;
                            }
                        }
                        _isCancelImport = true;
                        while (currentThread.isAlive()) {
                            currentThread.interrupt();
                            try {
                                Thread.sleep(10);
                            } catch (final InterruptedException ex) {
                            }
                        }
                    }
                }, "GarminCancelObserver");
                cancelObserver.start();
                try {
                    final FixedGPSGarminDataProcessor garminDataProcessor = new FixedGPSGarminDataProcessor();
                    final GPSSerialDevice serialDevice = new GPSSerialDevice();
                    serialDevice.init(environment);
                    garminDataProcessor.setGPSDevice(serialDevice);
                    garminDataProcessor.open();
                    final String monitorDevInfo;
                    final String[] gpsInfo = garminDataProcessor.getGPSInfo();
                    if (gpsInfo != null && gpsInfo.length > 0) {
                        monitorDevInfo = Messages.Garmin_Transfer_msg + gpsInfo[0];
                    } else {
                        monitorDevInfo = Messages.Garmin_unknown_device;
                    }
                    final GarminProduct productInfo = garminDataProcessor.getGarminProductInfo(2000L);
                    garminDataProcessor.addProgressListener(new ProgressListener() {

                        private int done;

                        public void actionEnd(final String action_id) {
                        }

                        public void actionProgress(final String action_id, final int current_value) {
                            monitor.worked(current_value - done);
                            done = current_value;
                        }

                        public void actionStart(final String action_id, final int min_value, final int max_value) {
                            done = 0;
                            monitor.beginTask(monitorDevInfo, max_value);
                        }
                    });
                    final List<GarminTrack> tracks = garminDataProcessor.getTracks();
                    garminDataProcessor.close();
                    if (tracks != null) {
                        GarminTrack srcTrack = null;
                        final List<GarminTrack> destTracks = new ArrayList<GarminTrack>();
                        for (final GarminTrack track : tracks) {
                            if (track.getIdentification().equals("ACTIVE LOG")) {
                                srcTrack = track;
                            } else {
                                destTracks.add(track);
                            }
                        }
                        if (srcTrack != null && destTracks.size() > 0) {
                            mergeActiveLog(srcTrack, destTracks);
                        }
                        for (final GarminTrack track : destTracks) {
                            GarminTrackpointAdapter prevGta = null;
                            for (final Iterator<GPSTrackpoint> wpIter = track.getWaypoints().iterator(); wpIter.hasNext(); ) {
                                final GPSTrackpoint wp = wpIter.next();
                                if (wp instanceof GarminTrackpointAdapter) {
                                    final GarminTrackpointAdapter gta = (GarminTrackpointAdapter) wp;
                                    if (!gta.hasValidDistance()) {
                                        if (prevGta != null) {
                                            gta.setDistance(prevGta.getDistance() + MtMath.distanceVincenty(prevGta.getLatitude(), prevGta.getLongitude(), gta.getLatitude(), gta.getLongitude()));
                                        } else {
                                            gta.setDistance(0);
                                        }
                                    }
                                    prevGta = gta;
                                }
                            }
                            final VelocityContext context = new VelocityContext();
                            final ArrayList<GarminTrack> tList = new ArrayList<GarminTrack>();
                            tList.add(track);
                            context.put("tracks", tList);
                            context.put("printtracks", new Boolean(true));
                            context.put("printwaypoints", new Boolean(false));
                            context.put("printroutes", new Boolean(false));
                            final File receivedFile = new File(RawDataManager.getTempDir() + File.separator + track.getIdentification() + ".tcx");
                            final Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/gpx-template/tcx-2.0.vm"));
                            final Writer writer = new FileWriter(receivedFile);
                            addValuesToContext(context, productInfo);
                            Velocity.evaluate(context, writer, "MyTourbook", reader);
                            writer.close();
                            _receivedFiles.add(receivedFile);
                        }
                    }
                } catch (final Exception ex) {
                    if (!_isCancelImport) {
                        final Display display = PlatformUI.getWorkbench().getDisplay();
                        Runnable runnable;
                        if (ex instanceof GPSException && ex.getMessage().equals("Garmin device does not respond!")) {
                            runnable = new Runnable() {

                                public void run() {
                                    MessageDialog.openError(display.getActiveShell(), Messages.Garmin_data_transfer_error, Messages.Garmin_no_connection);
                                }
                            };
                        } else {
                            runnable = new Runnable() {

                                public void run() {
                                    ex.printStackTrace();
                                    ErrorDialog.openError(display.getActiveShell(), Messages.Garmin_data_transfer_error, Messages.Garmin_error_receiving_data, new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.Garmin_commuication_error, ex));
                                }
                            };
                        }
                        display.syncExec(runnable);
                    }
                } finally {
                    cancelObserver.interrupt();
                }
            }
