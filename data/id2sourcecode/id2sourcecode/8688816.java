    protected void onIncomingFileTransfer(DccFileTransfer transfer) {
        File file = new File("crosswords\\" + transfer.getFile().getName());
        String filename = file.getName();
        String[] nameAndExtension = splitString(filename, ".");
        if (nameAndExtension[nameAndExtension.length - 1].equalsIgnoreCase("ECW") && transfer.getSize() != -1 && transfer.getSize() < 5120) {
            if (!file.exists()) {
                transfer.receive(file, false);
            } else {
                if (overwriteFlag) {
                    file.delete();
                    transfer.receive(file, false);
                    sendMessage(transfer.getNick(), file.getName() + " already exists, but has been overwritten.  To disable file overwriting send me a private message " + "\"disable overwrite\" (Only approved nicks such as yours can do this).");
                } else {
                    sendMessage(transfer.getNick(), file.getName() + " already exists.  To enable file overwriting send me a private message " + "\"enable overwrite\" (Only approved nicks such as yours can do this).");
                }
            }
        } else sendMessage(transfer.getNick(), "Transfer denied");
    }
