    private String visit(CommonToken token) {
        if (token == null) {
            return "<nil />";
        }
        StringTemplate template = TEMPLATES.getInstanceOf("skribler/ast/token");
        template.setAttribute("type", token.getType());
        template.setAttribute("line", token.getLine());
        template.setAttribute("charPositionInLine", token.getCharPositionInLine());
        template.setAttribute("channel", token.getChannel());
        template.setAttribute("index", token.getTokenIndex());
        template.setAttribute("start", token.getStartIndex());
        template.setAttribute("stop", token.getStopIndex());
        template.setAttribute("text", token.getText());
        return template.toString();
    }
