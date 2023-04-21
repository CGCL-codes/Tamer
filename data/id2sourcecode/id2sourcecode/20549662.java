    @SuppressWarnings("serial")
    public WicketAbstractChat(final PageParameters parameters) {
        final Message model = new Message();
        final Form formChat = new Form("chatForm", new CompoundPropertyModel(model));
        final TextField field = new TextField("user");
        field.setOutputMarkupId(false);
        formChat.add(field);
        final Label chat = new Label("chat");
        chat.setOutputMarkupId(true);
        getChannelService().addChannelListener(this, "chat", new IChannelListener() {

            public void onEvent(final String channel, final Map<String, String> datas, final IChannelTarget target) {
                target.appendJavascript("document.getElementById('" + chat.getMarkupId() + "').innerHTML += '<br/>" + datas.get("message") + "'");
            }
        });
        formChat.add(chat);
        final TextField mess = new TextField("message");
        mess.setOutputMarkupId(true);
        formChat.add(mess);
        formChat.add(new AjaxButton("send", formChat) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form form) {
                final String currentChat = ((Message) form.getModelObject()).getUser() + " said " + ((Message) form.getModelObject()).getMessage();
                final ChannelEvent event = new ChannelEvent("chat");
                event.addData("message", currentChat);
                getChannelService().publish(event);
                target.appendJavascript("document.getElementById('" + mess.getMarkupId() + "').value =''");
                target.focusComponent(mess);
            }
        });
        add(formChat);
    }
