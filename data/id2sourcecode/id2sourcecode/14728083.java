    private String getTokenText(CommonTree tree) {
        if (tree != null) {
            System.out.println("tree not null");
            Token token = tree.getToken();
            if (tree.getToken() != null) {
                System.out.println("Token: " + token);
                System.out.println("Token cannel: " + token.getChannel());
                System.out.println("Token class: " + token.getClass());
                String tokenText = null;
                try {
                    tokenText = token.getText();
                    return tokenText;
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
