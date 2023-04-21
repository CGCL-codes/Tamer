    private String getChannel_4003Text(View view) {
        Channel domainModelElement = (Channel) view.getElement();
        if (domainModelElement != null) {
            return domainModelElement.getTitle();
        } else {
            NetworkDiagramEditorPlugin.getInstance().logError("No domain element for view with visualID = " + 4003);
            return "";
        }
    }
