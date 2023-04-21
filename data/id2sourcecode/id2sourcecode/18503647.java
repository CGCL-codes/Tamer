            @Override
            protected void populateItem(final ListItem item) {
                final OverrideBean overmodel = (OverrideBean) item.getModelObject();
                item.setModel(new CompoundPropertyModel(overmodel));
                final RadioGroup overrideTypeRadios = new RadioGroup("overrideType");
                final Radio localOverride = new Radio("localOverride", new Model(OverrideTypes.LOCAL_OVERRIDE));
                overrideTypeRadios.add(localOverride);
                final Radio queuedOverride = new Radio("queuedOverride", new Model(OverrideTypes.QUEUED_OVERRIDE));
                overrideTypeRadios.add(queuedOverride);
                final Radio remoteOverride = new Radio("remoteOverride", new Model(OverrideTypes.REMOTE_OVERRIDE));
                overrideTypeRadios.add(remoteOverride);
                item.add(overrideTypeRadios);
                final DropDownChoice username = new DropDownChoice("supervisor", LoggedUsers.LOGGED_USERS);
                item.add(username);
                final TextField password = new TextField("password");
                password.setOutputMarkupId(true);
                password.setEnabled(false);
                item.add(password);
                overrideTypeRadios.add(new AjaxFormChoiceComponentUpdatingBehavior() {

                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        final String overridetype = overrideTypeRadios.getModelObjectAsString();
                        if (StringUtils.equals(overridetype, OverrideTypes.LOCAL_OVERRIDE)) {
                            password.setEnabled(true);
                        } else {
                            password.setEnabled(false);
                        }
                        target.addComponent(password);
                    }
                });
                final AjaxButton submit = new AjaxButton("submit", ovrdForm) {

                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form form) {
                        final String overridetype = overrideTypeRadios.getModelObjectAsString();
                        final String toUser = ((OverrideBean) item.getModelObject()).getSupervisor();
                        if (StringUtils.isEmpty(toUser)) {
                            target.appendJavascript("sxicometd.alerts.failed('Please select supervisor name')");
                            return;
                        }
                        final int actualCnt = trackingService.countOverrideEntry(refNo);
                        if (actualCnt >= reqOvrdCnt) {
                            target.appendJavascript("sxicometd.alerts.failed('Required override reach.')");
                            return;
                        }
                        params.setSubmittedTo(toUser);
                        params.setRefNo(refNo);
                        if (StringUtils.equals(overridetype, OverrideTypes.REMOTE_OVERRIDE)) {
                            final OverrideModel remotemodel = new OverrideModel();
                            params.setOvrdkey(OverrideGenerator.generateOvrdKey());
                            remotemodel.setOvrdType(OverrideTypes.REMOTE_OVERRIDE);
                            serializeObject(baseModel, remotemodel);
                            boolean _success = overrideService.save(remotemodel, params);
                            if (_success) {
                                try {
                                    createEntry(params);
                                    final ChannelEvent event = new ChannelEvent(toUser);
                                    event.addData("Ref No.", refNo);
                                    event.addData(RemoteConstants.OUTGOING, "true");
                                    event.addData("tranCode", _txn.getTranCode());
                                    event.addData("tranType", _txn.getTranType());
                                    event.addData("tranAmt", String.valueOf(_txn.getTranAmt()));
                                    event.addData("tranDscp", _txn.getTranDscp());
                                    event.addData("RequestFrom", getUser());
                                    event.addData("RequestTo", toUser);
                                    getChannelService().publish(event);
                                    target.appendJavascript("sxicometd.alerts.success('" + CometdUtils.requestAlertSuccess(event.getData()) + "')");
                                } catch (OverrideException e) {
                                    target.appendJavascript("sxicometd.alerts.failed('" + e.getMessage() + "')");
                                    log.error(e);
                                }
                            } else {
                                target.appendJavascript("sxicometd.alerts.failed('Override request failed')");
                            }
                        }
                        if (StringUtils.equals(overridetype, OverrideTypes.QUEUED_OVERRIDE)) {
                            params.setOvrdkey(OverrideGenerator.generateOvrdKey());
                            final OverrideModel queuedmodel = new OverrideModel();
                            queuedmodel.setOvrdType(OverrideTypes.QUEUED_OVERRIDE);
                            serializeObject(baseModel, queuedmodel);
                            boolean _success = overrideService.save(queuedmodel, params);
                            if (_success) {
                                try {
                                    createEntry(params);
                                    final Map<String, String> alert = new HashMap<String, String>();
                                    alert.put("Ref No.", refNo);
                                    alert.put("tranCode", _txn.getTranCode());
                                    alert.put("tranType", _txn.getTranType());
                                    alert.put("tranAmt", String.valueOf(_txn.getTranAmt()));
                                    alert.put("tranDscp", _txn.getTranDscp());
                                    target.appendJavascript("sxicometd.alerts.success('" + CometdUtils.requestAlertSuccess(alert) + "')");
                                } catch (OverrideException e) {
                                    log.error(e);
                                    target.appendJavascript("sxicometd.alerts.failed('" + e.getMessage() + "')");
                                }
                            } else {
                                target.appendJavascript("sxicometd.alerts.failed('Override request failed')");
                            }
                        }
                        if (StringUtils.equals(overridetype, OverrideTypes.LOCAL_OVERRIDE)) {
                            params.setOvrdkey(OverrideGenerator.generateOvrdKey());
                            params.setOvrdType(OverrideTypes.LOCAL_OVERRIDE);
                            try {
                                final OverrideHeader header = trackingService.findOverrideHeader(refNo);
                                if (header.getOvrdStatus() == OverrideConstants.OVRD_STATUS_REJECTED) {
                                    target.appendJavascript("sxicometd.alerts.failed('Transaction has already been rejected')");
                                    return;
                                }
                                createEntry(params);
                                boolean _check = trackingService.checkLocalOverride(params.getRefNo(), params.getOvrdkey());
                                if (_check) {
                                    final Map<String, String> alert = new HashMap<String, String>();
                                    alert.put("Ref No.", refNo);
                                    alert.put("tranCode", _txn.getTranCode());
                                    alert.put("tranType", _txn.getTranType());
                                    alert.put("tranAmt", String.valueOf(_txn.getTranAmt()));
                                    alert.put("tranDscp", _txn.getTranDscp());
                                    target.appendJavascript("sxicometd.alerts.success('" + CometdUtils.requestAlertSuccess(alert) + "')");
                                } else {
                                    target.appendJavascript("sxicometd.alerts.failed('Override request failed')");
                                }
                            } catch (OverrideException e) {
                                log.error(e);
                                target.appendJavascript("sxicometd.alerts.failed('" + e.getMessage() + "')");
                            }
                        }
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target, Form form) {
                        target.addComponent(feedback);
                        super.onError(target, form);
                    }
                };
                item.add(submit);
                item.add(new AjaxButton("cancel", ovrdForm) {

                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form form) {
                    }
                }.setDefaultFormProcessing(false));
            }
