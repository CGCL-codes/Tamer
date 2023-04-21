            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form form) {
                final String currentChat = ((Message) form.getModelObject()).getUser() + " said " + ((Message) form.getModelObject()).getMessage();
                final ChannelEvent event = new ChannelEvent("chat");
                event.addData("message", currentChat);
                getChannelService().publish(event);
                target.appendJavascript("document.getElementById('" + mess.getMarkupId() + "').value =''");
                target.focusComponent(mess);
            }
