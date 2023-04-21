    public static Element setContentEditable(Element element, boolean isEditable, boolean whiteSpacePreWrap) {
        if (UserAgent.isSafari()) {
            JsoView.as(element.getStyle()).setString("-webkit-user-modify", isEditable ? "read-write-plaintext-only" : "read-only");
        } else {
            element.setAttribute("contentEditable", isEditable ? "true" : "false");
        }
        if (whiteSpacePreWrap) {
            JsoView.as(element.getStyle()).setString("white-space", "pre-wrap");
        }
        return element;
    }
