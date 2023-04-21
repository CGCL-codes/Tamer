    private void dispatchUserUtterance(UserUtterance userUtterance) {
        try {
            ToolOutput input = inputProcessor.processInput(userUtterance.getText(), getId(), getUser().getId());
            if (input != null) {
                if (useJabber) {
                    final URI commChannel = input.toolHas("hasOutput", "CommunicationChannel");
                    input.hasTextValue(commChannel, userUtterance.getChannel().toString());
                }
                mind.processInput(input);
            }
        } catch (RdfException e) {
            if (log.isErrorEnabled()) {
                log.error("cannot add channel information to user input", e);
            }
        }
    }
