    private static void init() {
        isInitialised = true;
        String line = null;
        BufferedReader input_stream = null;
        ClassLoader loader = Messages.class.getClassLoader();
        String enc = "Cp1252";
        int equalsIndex;
        try {
            messages = new HashMap();
            String targetFile = "messages.properties";
            String ID = bundle.getLocale().toString();
            if (ID.startsWith("en")) {
                targetFile = "messages.properties";
            } else {
                targetFile = "messages_" + ID + ".properties";
            }
            input_stream = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/international/" + targetFile), enc));
            if (input_stream == null) LogWriter.writeLog("Unable to open messages.properties from jar");
            while (true) {
                line = input_stream.readLine();
                if (line == null) break;
                equalsIndex = line.indexOf('=');
                if (equalsIndex != -1) {
                    String message = line.substring(equalsIndex + 1);
                    StringBuffer newMessage = new StringBuffer();
                    StringTokenizer t = new StringTokenizer(message, "\\&;", true);
                    String nextValue = "";
                    boolean isAmpersand = false;
                    while (t.hasMoreTokens()) {
                        if (isAmpersand) {
                            nextValue = "&";
                            isAmpersand = false;
                        } else nextValue = t.nextToken();
                        if (nextValue.equals("\\")) {
                            String ascii = t.nextToken();
                            char c = ascii.charAt(0);
                            if (c == 'n') {
                                newMessage.append('\n');
                            } else if (c == ' ') {
                                newMessage.append(' ');
                            } else {
                                System.out.println("no value for " + c);
                                System.exit(1);
                            }
                            newMessage.append(ascii.substring(1));
                        } else if (nextValue.equals("&")) {
                            String ascii = t.nextToken();
                            String end;
                            if (t.hasMoreTokens()) {
                                end = t.nextToken();
                                if (end.equals("&")) {
                                    newMessage.append("&");
                                    newMessage.append(ascii);
                                    isAmpersand = true;
                                } else if (end.equals(";")) {
                                    if (ascii.startsWith("#")) ascii = ascii.substring(1);
                                    char c = (char) Integer.parseInt(ascii);
                                    newMessage.append(c);
                                } else {
                                    {
                                        if (t.hasMoreTokens()) newMessage.append("&");
                                        newMessage.append(ascii);
                                    }
                                }
                            } else {
                                newMessage.append("&");
                                newMessage.append(ascii);
                            }
                        } else newMessage.append(nextValue);
                    }
                    messages.put(line.substring(0, equalsIndex), newMessage.toString());
                }
            }
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " reading message Bundle");
            e.printStackTrace();
        }
        if (input_stream != null) {
            try {
                input_stream.close();
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " reading lookup table for pdf  for abobe map");
            }
        }
    }
