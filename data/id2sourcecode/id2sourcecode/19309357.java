    private String makeString() {
        if (pattern.equals("file") && fileSupport) ft.processMessage(message);
        if (pattern.equals("mp3") && mp3Support) mp3.processMessage(message);
        if (pattern.equals("top") && toplistSupport) toplist.processMessage(message);
        if (pattern.equals("help")) asw.print(help.mainHelp(JID), false, from);
        if (pattern.equals("version")) asw.print("Version: 0.2.1-Alpha", false, from);
        if (pattern.equals("roll")) asw.print(nick + " hat gewürfelt: " + (new Random().nextInt(6) + 1), !message.getType().equals(message.getType().valueOf("chat")), from);
        if (pattern.equals("bier")) asw.print(nick + " gibt ne Runde Bier aus!", !message.getType().equals(message.getType().valueOf("chat")), from);
        if (pattern.equals("upupdowndownleftrightleftrightbabaselectstart")) asw.print(nick + " hat 90 Continues bekommen!", !message.getType().equals(message.getType().valueOf("chat")), from);
        if (pattern.equals("quote")) {
            String Output = "Random GBO Quote:";
            try {
                InputStream is = null;
                URL url = new URL("http://www.german-bash.org/action/random");
                URLConnection connection = url.openConnection();
                is = connection.getInputStream();
                String page = new Scanner(is).useDelimiter("\\Z").next();
                is.close();
                String[] test = page.split("<div class=\"zitat\">");
                String[] bam = test[1].split("</span>");
                for (int i = 0; i < (bam.length) - 1; i++) {
                    bam[i] = bam[i].replaceAll("<.*?>", "");
                    bam[i] = bam[i].replaceAll("&lt;", "<");
                    bam[i] = bam[i].replaceAll("&gt;", ">");
                    bam[i] = bam[i].replaceAll("&quot;", "\"");
                    bam[i] = bam[i].trim();
                    if (!bam[i].equals("")) Output = Output + "\n" + bam[i];
                }
            } catch (Exception ex) {
                System.out.println("URL fehlerhaft");
            }
            asw.print(Output, !message.getType().equals(message.getType().valueOf("chat")), from);
        }
        return "";
    }
