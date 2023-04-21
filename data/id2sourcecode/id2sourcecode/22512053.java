    public ViewOverridePage(PageParameters parameters, final OverrideModel model) {
        super(parameters);
        final TestTransaction _txn = (TestTransaction) SerializationUtils.deserialize(model.getHdrMdl());
        add(new Label("tranCode", _txn.getTranCode()));
        add(new Label("tranType", _txn.getTranType()));
        add(new Label("tranAmt", _txn.getTranAmt().toString()));
        add(new Label("tranDscp", _txn.getTranDscp()));
        final OverrideBean ovrdbean = new OverrideBean();
        ovrdbean.setSupervisor(getUser());
        final Form form = new Form("supform", new CompoundPropertyModel(ovrdbean));
        add(form);
        form.add(new Label("supervisor"));
        final RequiredTextField pass = new RequiredTextField("password");
        form.add(pass);
        final String requestee = model.getRequestBy();
        final OverrideHeader ovrdHead = trackingService.findOverrideHeader(model.getRefNo());
        final AjaxButton accept = new AjaxButton("accept", form) {

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
        };
        form.add(accept);
        final AjaxButton reject = new AjaxButton("reject", form) {

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
        };
        form.add(reject);
    }
