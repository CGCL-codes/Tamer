    public void write(Message message) throws IOException {
        if (this.startDate == null) {
            writeHeader();
            this.startDate = message.getDate();
            this.lastDate = this.startDate;
        }
        long date = message.getDate().getTime() / 1000 - this.lastDate.getTime() / 1000;
        this.lastDate = message.getDate();
        String chan = message.getChannel();
        if (!this.channels.contains(chan)) this.channels.add(chan);
        String src = message.getAvatar();
        if (!this.sources.contains(src)) this.sources.add(src);
        this.out.println("\t[" + date + "," + this.channels.indexOf(chan) + "," + this.sources.indexOf(src) + ",\"" + escape(message.getContent()) + "\"],");
    }
