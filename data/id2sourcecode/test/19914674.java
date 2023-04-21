    public void error() {
        disconnect();
        application.message("lights failure, time out!", null, null);
        Util.debug("lights failure, time out!", this);
        new SendMail("lights error", "lights failure, time out, disconnecting with command buffer: " + " read delta: " + getReadDelta() + " write delta: " + getWriteDelta());
    }
