    public void show(RepInfo info) {
        AuditState state = (AuditState) _stateStack.peek();
        if (info.getModule() == null) {
            state.setNotFound(state.getNotFound() + 1);
            _writer.println("<!-- file not found or not readable: " + info.getUri() + " -->");
        } else {
            String mime = info.getMimeType();
            AuditCount count = (AuditCount) _mimeType.get(mime);
            if (count == null) {
                count = new AuditCount();
            }
            int valid = info.getValid();
            if (valid == RepInfo.TRUE) {
                state.setValid(state.getValid() + 1);
                count.setValid(count.getValid() + 1);
            } else {
                state.setWellFormed(state.getWellFormed() + 1);
                count.setWellFormed(count.getWellFormed() + 1);
            }
            _mimeType.put(mime, count);
        }
        showImpl(info, state);
    }
