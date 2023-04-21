    private void process(Operation task, int require, int length) throws IOException {
        SelectableChannel channel = task.getChannel();
        int hash = channel.hashCode();
        list[hash % length].process(task, require);
    }
