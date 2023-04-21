    public static void main(String[] args) {
        try {
            forecastPattern = new RE("Vorhersage:</b> *\r?\n</p>" + " *\r?\n<p align=justify>" + " *\r?\n(.*)</p></p>.*" + "<p><b>Weitere Aussichten:</b>" + " *\r?\n<p align=justify> *\r?\n" + "([^<]*)</p></p>", RE.REG_MULTILINE | RE.REG_DOT_NEWLINE);
            URL url = new URL("http://www.dwd.de/ext/e2/dlwett.htm");
            System.out.println("reading weather forecast");
            Reader r = new InputStreamReader(url.openStream());
            StringBuffer sb = new StringBuffer();
            int read;
            do {
                read = r.read();
                if (read != -1) sb.append((char) read);
            } while (read != -1);
            r.close();
            System.out.println("extracting forecast and outlook");
            REMatch match = forecastPattern.getMatch(sb);
            System.out.println("sending as SMS to " + args[0]);
            String forecast = strip(match.toString(1));
            String outlook = strip(match.toString(2));
            new SMSSender().sendLong(args[0], forecast + "; " + outlook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
