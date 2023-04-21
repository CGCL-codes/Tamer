    @Override
    public IRunnableWithProgress createImportRunnable(final String portName, final List<File> receivedFiles) {
        _receivedFiles = receivedFiles;
        return new IRunnableWithProgress() {

            /**
			 * Adds some important values to the velocity context (e.g. date, ...).
			 *
			 * @param context
			 *            the velocity context holding all the data
			 * @param productInfo
			 *            infos about the Garmin device
			 */
            private void addValuesToContext(final VelocityContext context, final GarminProduct productInfo) {
                final DecimalFormat double6formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                double6formatter.applyPattern("0.0000000");
                final DecimalFormat int_formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                int_formatter.applyPattern("000000");
                final DecimalFormat double2formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                double2formatter.applyPattern("0.00");
                final OneArgumentMessageFormat string_formatter = new OneArgumentMessageFormat("{0}", Locale.US);
                final SimpleDateFormat dateFormat = new SimpleDateFormat();
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                context.put("dateformatter", dateFormat);
                context.put("double6formatter", double6formatter);
                context.put("intformatter", int_formatter);
                context.put("stringformatter", string_formatter);
                context.put("double2formatter", double2formatter);
                final Calendar now = Calendar.getInstance();
                final Date creationDate = now.getTime();
                context.put("creation_date", creationDate);
                context.put("author", System.getProperty(DataTransferWizard.SYSPROPERTY_IMPORT_PERSON, "MyTourbook"));
                final String productName = productInfo.getProductName();
                context.put("devicename", productName.substring(0, productName.indexOf(' ')));
                context.put("productid", "" + productInfo.getProductId());
                context.put("devicemajorversion", "" + (productInfo.getProductSoftware() / 100));
                context.put("deviceminorversion", "" + (productInfo.getProductSoftware() % 100));
                String pluginmajorversion = "0";
                String pluginminorversion = "0";
                final Version version = Activator.getDefault().getVersion();
                if (version != null) {
                    pluginmajorversion = "" + version.getMajor();
                    pluginminorversion = "" + version.getMinor();
                }
                context.put("pluginmajorversion", pluginmajorversion);
                context.put("pluginminorversion", pluginminorversion);
                double min_latitude = 90.0;
                double min_longitude = 180.0;
                double max_latitude = -90.0;
                double max_longitude = -180.0;
                final List<?> routes = (List<?>) context.get("routes");
                if (routes != null) {
                    final Iterator<?> route_iterator = routes.iterator();
                    while (route_iterator.hasNext()) {
                        final GPSRoute route = (GPSRoute) route_iterator.next();
                        min_longitude = route.getMinLongitude();
                        max_longitude = route.getMaxLongitude();
                        min_latitude = route.getMinLatitude();
                        max_latitude = route.getMaxLatitude();
                    }
                }
                final List<?> tracks = (List<?>) context.get("tracks");
                if (tracks != null) {
                    final Iterator<?> track_iterator = tracks.iterator();
                    while (track_iterator.hasNext()) {
                        final GPSTrack track = (GPSTrack) track_iterator.next();
                        min_longitude = Math.min(min_longitude, track.getMinLongitude());
                        max_longitude = Math.max(max_longitude, track.getMaxLongitude());
                        min_latitude = Math.min(min_latitude, track.getMinLatitude());
                        max_latitude = Math.max(max_latitude, track.getMaxLatitude());
                    }
                }
                final List<?> waypoints = (List<?>) context.get("waypoints");
                if (waypoints != null) {
                    final Iterator<?> waypoint_iterator = waypoints.iterator();
                    while (waypoint_iterator.hasNext()) {
                        final GPSWaypoint waypoint = (GPSWaypoint) waypoint_iterator.next();
                        min_longitude = Math.min(min_longitude, waypoint.getLongitude());
                        max_longitude = Math.max(max_longitude, waypoint.getLongitude());
                        min_latitude = Math.min(min_latitude, waypoint.getLatitude());
                        max_latitude = Math.max(max_latitude, waypoint.getLatitude());
                    }
                }
                context.put("min_latitude", new Double(min_latitude));
                context.put("min_longitude", new Double(min_longitude));
                context.put("max_latitude", new Double(max_latitude));
                context.put("max_longitude", new Double(max_longitude));
                Date starttime = null;
                Date endtime = null;
                int heartNum = 0;
                long heartSum = 0;
                int cadNum = 0;
                long cadSum = 0;
                short maximumheartrate = 0;
                double totaldistance = 0;
                for (final Object name2 : tracks) {
                    final GPSTrack track = (GPSTrack) name2;
                    for (final Iterator<?> wpIter = track.getWaypoints().iterator(); wpIter.hasNext(); ) {
                        final GPSTrackpoint wp = (GPSTrackpoint) wpIter.next();
                        if (wp.getDate() != null) {
                            if (starttime == null) {
                                starttime = wp.getDate();
                            }
                            endtime = wp.getDate();
                        }
                        if (wp instanceof GarminTrackpointAdapter) {
                            final GarminTrackpointAdapter gta = (GarminTrackpointAdapter) wp;
                            if (gta.hasValidHeartrate()) {
                                heartSum += gta.getHeartrate();
                                heartNum++;
                                if (gta.getHeartrate() > maximumheartrate) {
                                    maximumheartrate = gta.getHeartrate();
                                }
                            }
                            if (gta.hasValidCadence()) {
                                cadSum += gta.getCadence();
                                cadNum++;
                            }
                            if (gta.hasValidDistance()) {
                                totaldistance = gta.getDistance();
                            }
                        }
                    }
                }
                if (starttime != null) {
                    context.put("starttime", starttime);
                } else {
                    context.put("starttime", creationDate);
                }
                if (starttime != null && endtime != null) {
                    context.put("totaltime", ((double) endtime.getTime() - starttime.getTime()) / 1000);
                } else {
                    context.put("totaltime", (double) 0);
                }
                context.put("totaldistance", totaldistance);
                if (maximumheartrate != 0) {
                    context.put("maximumheartrate", maximumheartrate);
                }
                if (heartNum != 0) {
                    context.put("averageheartrate", heartSum / heartNum);
                }
                if (cadNum != 0) {
                    context.put("averagecadence", cadSum / cadNum);
                }
            }

            /**
			 * If in the tracks date or altitude values is missing, these are copied from activeLog.
			 *
			 * @param monitor
			 * @param activeLog
			 * @param tracks
			 */
            @SuppressWarnings("unchecked")
            private void mergeActiveLog(final GarminTrack activeLog, final List<GarminTrack> tracks) {
                final Map<ListIterator<GPSTrackpoint>, GPSTrackpoint> destinationTracks = new HashMap<ListIterator<GPSTrackpoint>, GPSTrackpoint>();
                for (final GarminTrack track : tracks) {
                    final ListIterator<GPSTrackpoint> lIter = ((List<GPSTrackpoint>) track.getWaypoints()).listIterator();
                    while (lIter.hasNext()) {
                        final GPSTrackpoint tp = lIter.next();
                        if (tp.getDate() == null || !tp.hasValidAltitude()) {
                            destinationTracks.put(lIter, tp);
                            break;
                        }
                    }
                }
                if (destinationTracks.size() > 0) {
                    for (final Object obj : activeLog.getWaypoints()) {
                        if (obj instanceof GPSTrackpoint) {
                            final GPSTrackpoint srcTp = (GPSTrackpoint) obj;
                            for (final Entry<ListIterator<GPSTrackpoint>, GPSTrackpoint> entry : destinationTracks.entrySet()) {
                                final GPSTrackpoint destTP = entry.getValue();
                                if (srcTp.getLongitude() == destTP.getLongitude() && srcTp.getLatitude() == destTP.getLatitude()) {
                                    final ListIterator<GPSTrackpoint> destIter = entry.getKey();
                                    if ((!destTP.hasValidAltitude()) && srcTp.hasValidAltitude()) {
                                        destTP.setAltitude(srcTp.getAltitude());
                                    }
                                    if (destTP.getDate() == null && srcTp.getDate() != null) {
                                        destTP.setDate(srcTp.getDate());
                                    }
                                    while (destIter.hasNext()) {
                                        final GPSTrackpoint tp = destIter.next();
                                        if (tp.getDate() == null || !tp.hasValidAltitude()) {
                                            entry.setValue(tp);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

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
        };
    }
