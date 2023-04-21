    private void parseOwnInfo(String s) {
        this.mainDialog.getBoard().parseOwnInfo(s);
        if (this.properties.isFibsReadyToPlay() != this.mainDialog.getBoard().isReady()) this.writeNetworkMessageln("toggle ready"); else this.mainDialog.setReadyToPlay(this.properties.isFibsReadyToPlay());
        this.mainDialog.redrawOwnInfo();
    }
