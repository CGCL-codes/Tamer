    protected String getStateString() {
        return (getName() + "connected = " + is_connected + ", closed = " + is_closed + ", " + "chan: reg = " + source_channel.isRegistered() + ", open = " + source_channel.isOpen() + ", " + "read:" + (proxy_read_state == null ? null : proxy_read_state.getStateName()) + ", " + "write:" + (proxy_write_state == null ? null : proxy_write_state.getStateName()) + ", " + "connect:" + (proxy_connect_state == null ? null : proxy_connect_state.getStateName()));
    }
