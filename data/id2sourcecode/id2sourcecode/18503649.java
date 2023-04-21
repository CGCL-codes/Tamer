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
