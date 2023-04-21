        public ChatForm(String wicketID, String channelID) {
            super(wicketID);
            this.channelID = channelID;
            messageCount = ((BGOSession) getSession()).getMessageCount();
            updateDisplay();
            final MultiLineLabel chatDisplay = new MultiLineLabel("chatDisplay", new PropertyModel(properties, "chatDisplay"));
            chatDisplay.setEscapeModelStrings(false);
            chatDisplay.setOutputMarkupId(true);
            chatDisplay.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));
            final TextField chatField = new TextField("chatField", new PropertyModel(properties, "chatField"));
            chatField.setOutputMarkupId(true);
            AjaxSubmitLink sendLink = new AjaxSubmitLink("sendLink", this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form form) {
                    String messageStr = properties.getString("chatField");
                    if (messageStr == null || messageStr.equals("")) {
                        return;
                    }
                    target.addComponent(chatField);
                    target.addComponent(chatDisplay);
                    properties.put("chatField", "");
                    target.appendJavascript("document.getElementById('" + chatField.getMarkupId() + "').focus();");
                    target.appendJavascript("document.getElementById('chatWrap').scrollBy(0,1000);");
                    ChatMessage msg = getChannel().postMessage(getUser(), messageStr);
                    if (msg == null) {
                        buffer.append(getLocalizer().getString("cantPost", page) + "\n");
                    }
                    updateDisplay();
                }
            };
            sendLink.add(new Button("sendButton", new Model(getLocalizer().getString("send", page))));
            add(chatDisplay);
            add(chatField);
            add(sendLink);
        }
