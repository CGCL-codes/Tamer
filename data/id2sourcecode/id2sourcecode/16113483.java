    private ConcreteElement getForm(String provider, RunData rundata) {
        DynamicURI duri = new DynamicURI(rundata);
        Form form = new Form().setAction(duri.toString());
        Table table = new Table().setBorder(0);
        form.addElement(table);
        ParameterParser params = rundata.getParameters();
        String topic = params.getString("topic", "");
        String title = params.getString("title", "");
        String link = params.getString("link", "");
        String description = params.getString("description", "");
        Content content = null;
        try {
            content = this.getContentMarkup(this.getURL(provider)).getContent();
        } catch (Exception e) {
            logger.error("Exception", e);
            return new StringElement("Can't use this provider: " + e.getMessage());
        }
        Select select = new Select();
        select.setName("topic");
        Entry[] topics = content.getChannel().getTopics().getEntry();
        for (int i = 0; i < topics.length; ++i) {
            String name = topics[i].getName();
            select.addElement(new Option(name).addElement(name));
        }
        table.addElement(getRow("Topic: ", select));
        table.addElement(getRow("Title: ", new Input().setType("text").setName("title").setValue(title)));
        table.addElement(getRow("Link: ", new Input().setType("text").setName("link").setValue(link)));
        table.addElement(new TR().addElement(new TD().setColSpan(2).addElement(new TextArea().setName("description").setCols(65).setRows(15).addElement(description))));
        form.addElement(new Input().setType("hidden").setName(PROVIDER_NAME_KEY).setValue(provider));
        form.addElement(new Input().setType("submit").setName(POST_ARTICLE).setValue(POST_ARTICLE));
        return new Center(form);
    }
