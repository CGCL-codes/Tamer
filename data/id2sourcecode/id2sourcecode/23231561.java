    @Override
    public void run() {
        try {
            URL url = new URL("http://ohdict.com/translate-" + this.getArgs()[1] + ".html");
            URLConnection connection = url.openConnection();
            connection.connect();
            String cookie = connection.getHeaderField("Set-Cookie");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String decodedString;
            String auth_code = "";
            Pattern p = Pattern.compile("[0-9a-z]{32}'\\)\\;");
            while ((decodedString = in.readLine()) != null) {
                Matcher m = p.matcher(decodedString);
                if (m.find()) {
                    auth_code = m.group().substring(0, 32);
                    break;
                }
            }
            in.close();
            url = new URL("http://ohdict.com/ajax.php?ajax_display=content_page");
            connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("User-Agent", auth_code);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Referer", "http://ohdict.com/translate-" + this.getArgs()[1] + ".html");
            connection.setRequestProperty("Cookie", cookie);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write("seekname=" + this.getArgs()[1]);
            out.write("&");
            out.write("cateId=1");
            out.write("&");
            out.write("dictId=1");
            out.close();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String word = "";
            String meaning = "";
            boolean shouldContinue = false;
            int numContinue = 0;
            p = Pattern.compile(">.*<\\/sp");
            String output = "";
            decodedString = in.readLine();
            while ((decodedString = in.readLine()) != null) {
                System.out.println("@@ " + decodedString);
                if (shouldContinue && numContinue != 3) {
                    numContinue++;
                    continue;
                } else if (numContinue == 3) {
                    String temp = decodedString.trim();
                    meaning = temp.substring(3, temp.length() - 4);
                    output += meaning + " | ";
                    shouldContinue = false;
                    numContinue = 0;
                }
                Matcher m = p.matcher(decodedString);
                if (m.find()) {
                    int leng = this.getArgs()[1].length();
                    word = m.group().substring(1, 1 + leng);
                    output += word + ": ";
                    shouldContinue = true;
                    continue;
                }
            }
            in.close();
            if (output.length() != 0) {
                this.getBot().sendMessage(this.getChannel(), this.getSender() + ", " + output + " 更详细的解释请按 " + "http://ohdict.com/translate-" + this.getArgs()[1] + ".html");
            } else {
                this.getBot().sendMessage(this.getChannel(), this.getSender() + ", 我找不到 " + this.getArgs()[1] + " 这个单词！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
