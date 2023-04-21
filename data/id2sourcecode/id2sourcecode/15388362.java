    protected void doExecute(Channel channel) throws CommException {
        read_data = channel.send(write_data, 1);
        for (int i = 0; i < write_data.length; i++) {
            if (read_data[i] != write_data[i]) {
                throw new CommException("Invalid return data");
            }
        }
        value = read_data[2] & 0xFF;
        controller.processFeedbackData(module, value);
    }
