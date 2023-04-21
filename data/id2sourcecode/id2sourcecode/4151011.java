    protected int getChannelServiceBindingCount() throws Exception {
        GetChannelServiceBindingCountTask task = new GetChannelServiceBindingCountTask();
        txnScheduler.runTask(task, taskOwner);
        return task.count;
    }
