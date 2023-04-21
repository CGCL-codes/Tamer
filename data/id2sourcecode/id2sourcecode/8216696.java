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
