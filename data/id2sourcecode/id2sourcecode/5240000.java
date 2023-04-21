    public void openAddItem(AddItemAttributes attributes) throws ProtocolException {
        addItemScriptWriter = new StringWriter();
        writeAddItemScript("Ticker.Response.addItem({");
        writeAddItemScript("id:" + createJavaScriptString(attributes.getItemId()));
        writeAddItemScript(",channel:" + createJavaScriptString(attributes.getChannel()));
        addedItemId = attributes.getItemId();
    }
