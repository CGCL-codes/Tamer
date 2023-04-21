    private void process(Operation task, int require, int length) throws Exception {
        SelectableChannel channel = task.getChannel();
        int hash = channel.hashCode();
        list[hash % length].process(task, require);
    }
