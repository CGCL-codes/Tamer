    @Override
    public void execute() throws IOException {
        try {
            QueuedTask qt = (QueuedTask) getIn().readObject();
            getOut().writeObject(NetworkAck.get(ProcessRunner.kill(ProcessRunner.genThreadName(qt)) ? NetworkAck.OK : NetworkAck.ERR + "Unable to kill specified task!"));
            getOut().flush();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
