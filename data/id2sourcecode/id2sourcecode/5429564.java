    private String createIndexHTML() {
        String[] logDates = getLogDates();
        StringBuffer contents = new StringBuffer();
        contents.append("<ul>");
        for (int i = 0; i < logDates.length; i++) {
            contents.append("<li><a href=\"?");
            contents.append(logDates[i]);
            contents.append("\">");
            contents.append(logDates[i]);
            contents.append("</a></li>");
        }
        contents.append("</ul>");
        return template.replaceAll("\\$\\{nick\\}", config.getNick()).replaceAll("\\$\\{channel\\}", config.getChannel()).replaceAll("\\$\\{server\\}", config.getServerName()).replaceAll("\\$\\{contents\\}", contents.toString());
    }
