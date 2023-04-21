            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                boolean reject = trackingService.rejectTransaction(ovrdHead, model.getOvrdKey());
                if (reject) {
                    final ChannelEvent event = new ChannelEvent(requestee);
                    event.addData(RemoteConstants.ACCEPTED, "false");
                    event.addData("Transaction", _txn.getTranType());
                    event.addData("Refereence Number", model.getRefNo());
                    event.addData("From", getUser());
                    getChannelService().publish(event);
                    model.setStatus(4);
                    overrideService.update(model);
                    setRedirect(true);
                    setResponsePage(ListOverridePage.class);
                } else {
                    target.appendJavascript("sxicometd.alerts.failed('Sorry, override failed.')");
                }
            }
