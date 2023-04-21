    FlapPacket(int seqnum, FlapCommand command) {
        DefensiveTools.checkNull(command, "command");
        this.seqnum = seqnum;
        this.channel = command.getChannel();
        this.block = null;
        this.command = command;
    }
