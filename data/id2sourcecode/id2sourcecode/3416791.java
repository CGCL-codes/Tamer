    private void unregister(SelectableChannelListener listener, int ops) {
        synchronized (regs) {
            Operations rOps = (Operations) regs.get(listener);
            if (rOps == null) return;
            int index = -1;
            switch(ops) {
                case SelectionKey.OP_READ:
                    if (rOps.readOp < 0) return;
                    index = rOps.readOp;
                    rOps.readOp = -1;
                    break;
                case SelectionKey.OP_WRITE:
                    if (rOps.writeOp < 0) return;
                    index = rOps.writeOp;
                    rOps.writeOp = -1;
                    break;
                case SelectionKey.OP_CONNECT:
                    if (rOps.connOp < 0) return;
                    index = rOps.connOp;
                    rOps.connOp = -1;
                    break;
                case SelectionKey.OP_ACCEPT:
                    if (rOps.accOp < 0) return;
                    index = rOps.accOp;
                    rOps.accOp = -1;
                    break;
            }
            if (rOps.readOp < 0 && rOps.writeOp < 0 && rOps.connOp < 0 && rOps.accOp < 0) regs.remove(listener);
            if (index >= 0) pool[index].unregister(listener, ops);
        }
        return;
    }
