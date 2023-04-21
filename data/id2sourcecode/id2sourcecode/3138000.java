    private boolean readSipcBody() {
        if (this.lastAction == ACTION_NONE) {
            if (this.curMessage.getContentLength() > 0) {
                this.contentLeft = this.curMessage.getContentLength();
                this.byteWriter.clear();
            } else {
                lastAction = ACTION_NONE;
                return true;
            }
        }
        for (; byteReader.hasRemaining() && this.contentLeft > 0; this.contentLeft--) this.byteWriter.writeByte((byteReader.readByte()));
        if (this.contentLeft == 0) {
            this.curMessage.setBody(new SipcBody(ConvertHelper.byte2String(this.byteWriter.toByteArray())));
            this.lastAction = ACTION_NONE;
            return true;
        } else {
            this.lastAction = ACTION_READ_CONTENT;
            return false;
        }
    }
