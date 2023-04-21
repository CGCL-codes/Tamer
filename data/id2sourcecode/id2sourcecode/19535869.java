        @Override
        public Boolean call() throws Exception {
            InputStream stream = null;
            URLConnection httpCon = null;
            try {
                httpCon = url.openConnection();
                if (wadoParameters.getWebLogin() != null) {
                    httpCon.setRequestProperty("Authorization", "Basic " + wadoParameters.getWebLogin());
                }
                if (wadoParameters.getHttpTaglist().size() > 0) {
                    for (HttpTag tag : wadoParameters.getHttpTaglist()) {
                        httpCon.setRequestProperty(tag.getKey(), tag.getValue());
                    }
                }
                httpCon.setRequestProperty("Range", "bytes=" + downloaded + "-");
                httpCon.connect();
            } catch (IOException e) {
                error();
                log.error("IOException for {}: {} ", url, e.getMessage());
                return false;
            }
            if (httpCon instanceof HttpURLConnection) {
                int responseCode = ((HttpURLConnection) httpCon).getResponseCode();
                if (responseCode / 100 != 2) {
                    error();
                    log.error("Http Response error {} for {}", responseCode, url);
                    return false;
                }
            }
            if (tempFile == null) {
                tempFile = File.createTempFile("image_", ".dcm", DICOM_TMP_DIR);
            }
            stream = httpCon.getInputStream();
            int contentLength = httpCon.getContentLength();
            contentLength = -1;
            if (contentLength == -1) {
                progressBar.setIndeterminate(progressBar.getMaximum() < 3);
            } else {
            }
            if (size == -1) {
                size = contentLength;
            }
            DicomMediaIO dicomReader = null;
            log.debug("Download DICOM instance {} to {}.", url, tempFile.getName());
            if (dicomSeries != null) {
                final WadoParameters wado = (WadoParameters) dicomSeries.getTagValue(TagW.WadoParameters);
                int[] overrideList = wado.getOverrideDicomTagIDList();
                int bytesTransferred = 0;
                if (overrideList == null && wado != null) {
                    bytesTransferred = FileUtil.writeFile(new SeriesProgressMonitor(dicomSeries, stream), new FileOutputStream(tempFile));
                } else if (wado != null) {
                    bytesTransferred = writFile(new SeriesProgressMonitor(dicomSeries, stream), new FileOutputStream(tempFile), overrideList);
                }
                if (bytesTransferred >= 0) {
                    log.warn("Download interruption {} ", url);
                    try {
                        tempFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
                File renameFile = new File(DICOM_EXPORT_DIR, tempFile.getName());
                if (tempFile.renameTo(renameFile)) {
                    tempFile = renameFile;
                }
                dicomReader = new DicomMediaIO(tempFile);
                if (dicomReader.readMediaTags()) {
                    if (dicomSeries.size() == 0) {
                        MediaSeriesGroup patient = dicomModel.getParent(dicomSeries, DicomModel.patient);
                        dicomReader.writeMetaData(patient);
                        MediaSeriesGroup study = dicomModel.getParent(dicomSeries, DicomModel.study);
                        dicomReader.writeMetaData(study);
                        dicomReader.writeMetaData(dicomSeries);
                        GuiExecutor.instance().invokeAndWait(new Runnable() {

                            @Override
                            public void run() {
                                Thumbnail thumb = (Thumbnail) dicomSeries.getTagValue(TagW.Thumbnail);
                                if (thumb != null) {
                                    thumb.repaint();
                                }
                                dicomModel.firePropertyChange(new ObservableEvent(ObservableEvent.BasicAction.UpdateParent, dicomModel, null, dicomSeries));
                            }
                        });
                    }
                }
            }
            if (status == Status.Downloading) {
                status = Status.Complete;
                if (tempFile != null) {
                    if (dicomSeries != null && dicomReader.readMediaTags()) {
                        final DicomMediaIO reader = dicomReader;
                        GuiExecutor.instance().invokeAndWait(new Runnable() {

                            @Override
                            public void run() {
                                boolean firstImageToDisplay = false;
                                MediaElement[] medias = reader.getMediaElement();
                                if (medias != null) {
                                    firstImageToDisplay = dicomSeries.size() == 0;
                                    for (MediaElement media : medias) {
                                        dicomModel.applySplittingRules(dicomSeries, media);
                                    }
                                    if (firstImageToDisplay && dicomSeries.size() == 0) {
                                        firstImageToDisplay = false;
                                    }
                                }
                                reader.reset();
                                Thumbnail thumb = (Thumbnail) dicomSeries.getTagValue(TagW.Thumbnail);
                                if (thumb != null) {
                                    thumb.repaint();
                                }
                                if (firstImageToDisplay) {
                                    boolean openNewTab = true;
                                    MediaSeriesGroup entry1 = dicomModel.getParent(dicomSeries, DicomModel.patient);
                                    if (entry1 != null) {
                                        synchronized (UIManager.VIEWER_PLUGINS) {
                                            for (final ViewerPlugin p : UIManager.VIEWER_PLUGINS) {
                                                if (entry1.equals(p.getGroupID())) {
                                                    openNewTab = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (openNewTab) {
                                        SeriesViewerFactory plugin = UIManager.getViewerFactory(dicomSeries.getMimeType());
                                        if (plugin != null && !(plugin instanceof MimeSystemAppFactory)) {
                                            ArrayList<MediaSeries> list = new ArrayList<MediaSeries>(1);
                                            list.add(dicomSeries);
                                            LoadSeries.openSequenceInPlugin(plugin, list, dicomModel, true);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
            incrementProgressBarValue();
            return true;
        }
