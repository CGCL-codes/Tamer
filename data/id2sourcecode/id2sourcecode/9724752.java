    public ExportPage(List<PatientModel> list) {
        super();
        StudyPermissionHelper studyPermissionHelper = StudyPermissionHelper.get();
        if (ExportPage.BaseCSS != null) add(CSSPackageResource.getHeaderContribution(ExportPage.BaseCSS));
        if (ExportPage.CSS != null) add(CSSPackageResource.getHeaderContribution(ExportPage.CSS));
        HashMap<Integer, ExportResult> results = getSession().getMetaData(EXPORT_RESULTS);
        exportInfo = new ExportInfo(list);
        if (results == null) {
            results = new HashMap<Integer, ExportResult>();
            getSession().setMetaData(EXPORT_RESULTS, results);
        }
        resultId = id_count++;
        ExportResult result = new ExportResult(resultId, getPage().getNumericId());
        results.put(resultId, result);
        destinationAET = getSession().getMetaData(LAST_DESTINATION_AET_ATTRIBUTE);
        add(CSSPackageResource.getHeaderContribution(ExportPage.class, "folder-style.css"));
        initDestinationAETs();
        final BaseForm form = new BaseForm("form");
        form.setResourceIdPrefix("export.");
        add(form);
        form.add(new Label("label", "DICOM Export"));
        form.addLabel("selectedItems");
        form.addLabel("selectedPats");
        form.add(new Label("selectedPatsValue", new PropertyModel<Integer>(exportInfo, "nrOfPatients")));
        form.add(new Label("deniedPatsValue", new PropertyModel<Integer>(exportInfo, "deniedNrOfPatients")).setVisible(studyPermissionHelper.isUseStudyPermissions()));
        form.addLabel("selectedStudies");
        form.add(new Label("selectedStudiesValue", new PropertyModel<Integer>(exportInfo, "nrOfStudies")));
        form.add(new Label("deniedStudiesValue", new PropertyModel<Integer>(exportInfo, "deniedNrOfStudies")).setVisible(studyPermissionHelper.isUseStudyPermissions()));
        form.addLabel("selectedSeries");
        form.add(new Label("selectedSeriesValue", new PropertyModel<Integer>(exportInfo, "nrOfSeries")));
        form.add(new Label("deniedSeriesValue", new PropertyModel<Integer>(exportInfo, "deniedNrOfSeries")).setVisible(studyPermissionHelper.isUseStudyPermissions()));
        form.addLabel("selectedInstances");
        form.add(new Label("selectedInstancesValue", new PropertyModel<Integer>(exportInfo, "nrOfInstances")));
        form.add(new Label("deniedInstancesValue", new PropertyModel<Integer>(exportInfo, "deniedNrOfInstances")).setVisible(studyPermissionHelper.isUseStudyPermissions()));
        form.addLabel("totInstances");
        form.add(new Label("totInstancesValue", new PropertyModel<Integer>(exportInfo, "totNrOfInstances")));
        form.add(new DropDownChoice<AE>("destinationAETs", destinationModel, destinationAETs, new IChoiceRenderer<AE>() {

            private static final long serialVersionUID = 1L;

            public Object getDisplayValue(AE ae) {
                if (ae.getDescription() == null) {
                    return ae.getTitle();
                } else {
                    return ae.getTitle() + "(" + ae.getDescription() + ")";
                }
            }

            public String getIdValue(AE ae, int idx) {
                return String.valueOf(idx);
            }
        }) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isEnabled() {
                return exportInfo.hasSelection() && isExportInactive();
            }
        }.setNullValid(false).setOutputMarkupId(true));
        form.addLabel("destinationAETsLabel");
        form.addLabel("exportResultLabel");
        form.add(new Label("exportResult", new AbstractReadOnlyModel<String>() {

            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                if (exportInfo.hasSelection()) {
                    r = getExportResults().get(resultId);
                    exportPerformed = true;
                    return (r == null ? getString("export.message.exportDone") : r.getResultString());
                } else {
                    return getString("export.message.noSelectionForExport");
                }
            }
        }) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onComponentTag(ComponentTag tag) {
                String cssClass = exportPerformed ? r == null ? "export_succeed" : r.failedRequests.size() == 0 ? "export_running" : "export_failed" : "export_nop";
                log.debug("Export Result CSS class: {}", cssClass);
                tag.getAttributes().put("class", cssClass);
                super.onComponentTag(tag);
            }
        }.setOutputMarkupId(true));
        form.add(new AjaxButton("export", new ResourceModel("export.exportBtn.text")) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isEnabled() {
                return exportInfo.hasSelection() && isExportInactive();
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                getSession().setMetaData(LAST_DESTINATION_AET_ATTRIBUTE, destinationAET);
                exportSelected();
                target.addComponent(form);
            }
        }.setOutputMarkupId(true));
        form.add(new AjaxButton("close", new ResourceModel("export.closeBtn.text")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                removeProgressProvider(getExportResults().remove(resultId), true);
                getPage().getPageMap().remove(ExportPage.this);
                target.appendJavascript("javascript:self.close()");
            }
        });
        form.add(new AjaxCheckBox("closeOnFinished", new IModel<Boolean>() {

            private static final long serialVersionUID = 1L;

            public Boolean getObject() {
                return closeOnFinished;
            }

            public void setObject(Boolean object) {
                closeOnFinished = object;
            }

            public void detach() {
            }
        }) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.addComponent(this);
            }
        }.setEnabled(exportInfo.hasSelection()));
        form.addLabel("closeOnFinishedLabel");
        form.add(new AjaxIntervalBehaviour(700) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onTimer(AjaxRequestTarget target) {
                log.debug("########### Export timer for resultId:", resultId);
                if (closeRequest) {
                    removeProgressProvider(getExportResults().remove(resultId), true);
                    getPage().getPageMap().remove(ExportPage.this);
                    target.appendJavascript("javascript:self.close()");
                    isClosed = true;
                } else {
                    ExportResult result = getExportResults().get(resultId);
                    result.updateRefreshed();
                    if (result != null && !result.isRendered) {
                        target.addComponent(form.get("exportResult"));
                        if (result.nrOfMoverequests == 0) {
                            target.addComponent(form.get("export"));
                            target.addComponent(form.get("destinationAETs"));
                            target.addComponent(form.get("downloadLink"));
                            if (closeOnFinished && result.failedRequests.isEmpty()) {
                                removeProgressProvider(getExportResults().remove(resultId), false);
                                getPage().getPageMap().remove(ExportPage.this);
                                target.appendJavascript("javascript:self.close()");
                            }
                        }
                        result.isRendered = true;
                    }
                }
            }
        });
        add(JavascriptPackageResource.getHeaderContribution(ExportPage.class, "popupcloser.js"));
        final Label downloadError = new Label("downloadError", new Model<String>(""));
        form.add(downloadError);
        form.add(new Link<Object>("downloadLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isEnabled() {
                return exportInfo.hasSelection() && isExportInactive();
            }

            @Override
            public void onClick() {
                final List<FileToExport> files = getFilesToExport();
                if (files == null || files.isEmpty()) {
                    downloadError.setDefaultModel(new ResourceModel("export.download.missingFile"));
                    return;
                }
                RequestCycle.get().setRequestTarget(new IRequestTarget() {

                    public void detach(RequestCycle requestCycle) {
                    }

                    public void respond(RequestCycle requestCycle) {
                        boolean success = false;
                        OutputStream out = null;
                        try {
                            Response response = requestCycle.getResponse();
                            byte[] buf = new byte[WebCfgDelegate.getInstance().getDefaultFolderPagesize()];
                            HashSet<Integer> sopHash = new HashSet<Integer>();
                            if (files.size() > 1) {
                                response.setContentType("application/zip");
                                ((WebResponse) response).setAttachmentHeader("dicom.zip");
                                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
                                out = zos;
                                for (FileToExport fto : files) {
                                    log.debug("Write file to zip:{}", fto.file);
                                    ZipEntry entry = new ZipEntry(getZipEntryName(fto.blobAttrs, sopHash));
                                    zos.putNextEntry(entry);
                                    writeDicomFile(fto.file, fto.blobAttrs, zos, buf);
                                    zos.closeEntry();
                                }
                            } else {
                                response.setContentType("application/dicom");
                                ((WebResponse) response).setAttachmentHeader(getTemplateParam(files.get(0).blobAttrs, "#sopIuid", sopHash) + ".dcm");
                                out = response.getOutputStream();
                                writeDicomFile(files.get(0).file, files.get(0).blobAttrs, out, buf);
                            }
                            success = true;
                        } catch (ZipException ze) {
                            log.warn("Problem creating zip file: " + ze);
                        } catch (ClientAbortException cae) {
                            log.warn("Client aborted zip file download: " + cae);
                        } catch (Exception e) {
                            log.error("An error occurred while attempting to stream zip file for download: ", e);
                        } finally {
                            logExport(files, success);
                            try {
                                if (out != null) out.close();
                            } catch (Exception ignore) {
                            }
                        }
                    }
                });
            }
        }.add(new Label("downloadLabel", new ResourceModel("export.downloadBtn.text"))).setOutputMarkupId(true));
    }
