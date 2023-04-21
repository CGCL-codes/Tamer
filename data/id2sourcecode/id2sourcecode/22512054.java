            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                boolean apprv = trackingService.acceptTransaction(ovrdHead, model.getOvrdKey());
                if (apprv) {
                    final ChannelEvent event = new ChannelEvent(requestee);
                    event.addData(RemoteConstants.ACCEPTED, "true");
                    event.addData("Transaction", model.getReason());
                    event.addData("Transaction Date", model.getTxnDt().toString());
                    event.addData("Reference Number", model.getRefNo());
                    event.addData("Supervisor", getUser());
                    getChannelService().publish(event);
                    model.setStatus(1);
                    overrideService.update(model);
                    setRedirect(true);
                    setResponsePage(ListOverridePage.class);
                } else {
                    target.appendJavascript("sxicometd.alerts.failed('Sorry, override failed.')");
                }
            }
