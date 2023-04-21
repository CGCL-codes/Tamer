    @Override
    protected final void writeData(StringBuffer sb, String signature, DataSample<E> value) {
        sb.append("dz.data-sample\n");
        sb.append("{\n");
        sb.append("channel.name=").append(getChannelName(signature)).append("\n");
        sb.append("channel.signature=").append(signature).append("\n");
        sb.append("channel.value=").append(getValueString(value)).append("\n");
        if (value.isError()) {
            sb.append("error=").append(getErrorString(value)).append("\n");
        }
        sb.append("timestamp=").append(getTimestamp(value.timestamp)).append("\n");
        sb.append("}\n");
    }
