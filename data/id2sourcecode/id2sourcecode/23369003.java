    String printSelectionKey(IoSocketHandler socketHandler) {
        return ConnectionUtils.printSelectionKey(socketHandler.getChannel().keyFor(selector));
    }
