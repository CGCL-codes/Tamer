    public void printWrapped(PrintWriter pw, int width, int nextLineTabStop, String text) {
        StringBuffer sb = new StringBuffer(text.length());
        renderWrappedText(sb, width, nextLineTabStop, text);
        pw.println(sb.toString());
    }
