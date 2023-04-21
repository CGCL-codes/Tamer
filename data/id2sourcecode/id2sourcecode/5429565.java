    private String createLogHTML(String date) throws IOException {
        StringBuffer contents = new StringBuffer();
        contents.append("<p><a href=\"./\">Index</a></p>");
        contents.append("<h2>IRC Log for ");
        contents.append(date);
        contents.append("</h2>");
        FileInputStream inputStream = new FileInputStream(new File(config.getOutputDir(), date + ".log"));
        try {
            contents.append(getStringByStream(inputStream));
        } finally {
            inputStream.close();
        }
        return template.replaceAll("\\$\\{nick\\}", config.getNick()).replaceAll("\\$\\{channel\\}", config.getChannel()).replaceAll("\\$\\{server\\}", config.getServerName()).replaceAll("\\$\\{contents\\}", contents.toString());
    }
