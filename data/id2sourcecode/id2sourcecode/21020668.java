    public static ElementEditability getContentEditability(Element element) {
        String editability = null;
        if (UserAgent.isSafari()) {
            JsoView style = JsoView.as(element.getStyle());
            editability = style.getString(WEBKIT_USER_MODIFY);
            if ("read-write-plaintext-only".equalsIgnoreCase(editability) || "read-write".equalsIgnoreCase(editability)) {
                return ElementEditability.EDITABLE;
            } else if (editability != null && !editability.isEmpty()) {
                return ElementEditability.NOT_EDITABLE;
            }
        }
        try {
            editability = element.getAttribute("contentEditable");
        } catch (JavaScriptException e) {
            String elementString = "<couldn't get element string>";
            String elementTag = "<couldn't get element tag>";
            try {
                elementString = element.toString();
            } catch (Exception exception) {
            }
            try {
                elementTag = element.getTagName();
            } catch (Exception exception) {
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Couldn't get the 'contentEditable' attribute for element '");
            sb.append(elementString).append("' tag name = ").append(elementTag);
            throw new RuntimeException(sb.toString(), e);
        }
        if (editability == null || editability.isEmpty()) {
            return ElementEditability.NEUTRAL;
        } else {
            return "true".equalsIgnoreCase(editability) ? ElementEditability.EDITABLE : ElementEditability.NOT_EDITABLE;
        }
    }
