    protected boolean updateListData() {
        String text = super.textComp.getText();
        if (text.length() > 0) {
            String initial = text.substring(0, 1);
            if (!initial.equals(this._old_initial)) {
                this._old_initial = initial;
                try {
                    URL url = new URL(this._url_base + text);
                    SAXBuilder sb = new SAXBuilder();
                    Document d = sb.build(url.openStream());
                    Element r = d.getRootElement();
                    this._prompts = new ArrayList<String>();
                    for (Element e : r.getChildren("item")) this._prompts.add(e.getAttributeValue("value"));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        ArrayList<String> match_list = new ArrayList<String>();
        String regex = "^" + text + ".*$";
        for (String item : this._prompts) if (item.matches(regex)) match_list.add(item);
        super.list.setListData(match_list.toArray(new String[0]));
        return true;
    }
