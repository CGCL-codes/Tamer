    protected void doExecute(Channel channel) throws CommException {
        read_data = channel.send(write_data);
        value = read_data[0] & 0xFF;
        controller.processFeedbackData(module, value);
    }
