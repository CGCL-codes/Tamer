    public SXICometdRemote(final PageParameters parameters) {
        final Form formChat = new Form("chatForm");
        final String userchannel = WicketCometdSession.get().getCometUser();
        final RequiredTextField touserf = new RequiredTextField("touser", new PropertyModel(this, "touser"));
        formChat.add(touserf);
        getChannelService().addChannelListener(this, userchannel, new RemoteListener());
        formChat.add(new AjaxButton("send", formChat) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form form) {
                String toUser = touserf.getModelObjectAsString();
                String fromUser = WicketCometdSession.get().getCometUser();
                final ChannelEvent event = new ChannelEvent(toUser);
                event.addData("From", fromUser);
                event.addData("Override Type", "Transaction Limit");
                event.addData("Function", "Cash Deposit");
                event.addData(RemoteConstants.OUTGOING, "true");
                getChannelService().publish(event);
            }
        });
        add(formChat);
    }
