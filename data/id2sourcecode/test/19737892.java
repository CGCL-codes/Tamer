    public void write(Message message) throws IOException {
        if (this.firstTime) {
            writeHeader();
            this.firstTime = false;
        }
        if (this.split.shouldSplit(message)) {
            beforeSplit();
            this.split.split();
        }
        String date = message.getDateFormat().format(message.getDate());
        String channel = message.getChannel();
        String source = message.getAvatar();
        String content = message.getContent();
        String hoverTRIEPatch = " onMouseOver=\"this.id='trhover'\" onMouseOut=\"this.id=''\"";
        String hoverTDIEPatch = " onMouseOver=\"this.id='tdhover'\" onMouseOut=\"this.id=''\"";
        this.out.print("<tr class=\"" + channel.replaceAll(" ", "_") + "\"" + hoverTRIEPatch + ">");
        this.out.print("<td class=\"date\" class=\"" + source + "\"" + hoverTDIEPatch + ">" + date + "</td>");
        this.out.print("<td class=\"channel\" class=\"" + source + "\"" + hoverTDIEPatch + ">" + channel + "</td>");
        this.out.print("<td class=\"source\" class=\"" + source + "\"" + hoverTDIEPatch + ">" + source + "</td>");
        this.out.print("<td class=\"message\" class=\"" + source + "\"" + hoverTDIEPatch + ">" + content + "</td>");
        this.out.println("</tr>");
    }
