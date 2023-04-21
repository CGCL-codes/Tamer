    private void renderMessage(IMarkupWriter writer, IRequestCycle cycle, IThreadedMessage msg, String name) {
        log.debug("rendering a single msg...");
        String cName = name + "message" + msg.getMsgID();
        writer.begin("li");
        writer.begin("a");
        writer.attribute("href", "#");
        writer.attribute("id", cName + "header");
        writer.attributeRaw("onclick", "return toggleView('" + cName + "');");
        writer.print(msg.getSubject() + "(" + msg.getMessageAuthor() + " - " + df.format(msg.getWrittenDate()) + ")");
        writer.end();
        writer.begin("span");
        writer.attribute("id", cName);
        writer.attribute("style", "display:none;");
        writer.begin("span");
        writer.attribute("id", cName + "body");
        writer.attribute("style", "display:block; width: 450px;");
        writer.begin("span");
        writer.attribute("id", cName + "bodylabel");
        writer.attribute("style", "display:block");
        writer.print(getMessage("msg-text"));
        writer.end();
        writer.begin("span");
        writer.attribute("id", cName + "text");
        writer.attribute("style", "display:block; margin-left: 10px");
        writer.print(msg.getMessage());
        writer.end();
        writer.end();
        log.debug("before the 'has user' check...");
        if (!isNull(this.getUserobj())) {
            writer.begin("span");
            writer.attribute("id", cName + "controls");
            writer.attribute("style", "display:block");
            writer.begin("a");
            writer.attribute("href", "#");
            writer.attributeRaw("onclick", "return toggleView('" + cName + "replycontrol');");
            writer.print(getMessage("reply"));
            writer.end();
            writer.end();
            writer.begin("span");
            writer.attribute("id", cName + "replycontrol");
            writer.attribute("style", "display:none");
            writer.print(getMessage("subject"));
            writer.begin("input");
            writer.attribute("type", "text");
            writer.attribute("name", cName + "msgsubject");
            writer.attribute("value", "re: " + msg.getSubject());
            writer.end();
            writer.beginEmpty("br");
            writer.begin("span");
            writer.attribute("class", name + "MessageTextHeader");
            writer.print(getMessage("msg-text"));
            writer.end();
            writer.begin("textarea");
            writer.attribute("cols", "75");
            writer.attribute("rows", "10");
            writer.attribute("name", cName + "msgtext");
            writer.end();
            writer.beginEmpty("br");
            writer.begin("input");
            writer.attribute("type", "button");
            writer.attribute("value", getMessage("reply"));
            writer.attributeRaw("onclick", "this.form." + name + "messageid.value=" + msg.getMsgID() + ";this.form." + name + "text.value=this.form." + cName + "msgtext.value;this.form." + name + "subject.value=this.form." + cName + "msgsubject.value;this.form.submit();");
            writer.end();
            writer.end();
        }
        writer.end();
        log.debug("attempting to get msg responses...");
        List l = msg.getResponses();
        if (l.size() > 0) {
            writer.begin("ul");
            for (int i = 0; i < l.size(); i++) {
                log.debug("rendering response #:" + i);
                IThreadedMessage response = (IThreadedMessage) l.get(i);
                if (!response.getMsgID().equals(msg.getMsgID())) {
                    log.debug("response's id didn't equal the msg id... responseid: " + response.getMsgID() + "; msgid: " + msg.getMsgID());
                    this.renderMessage(writer, cycle, response, name);
                }
            }
            writer.end();
        }
        log.debug("finished rendering responses.");
        writer.end();
    }
