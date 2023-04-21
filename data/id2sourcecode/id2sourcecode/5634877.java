    static void checkToplist() {
        if (integrityCheck()) {
            Util.debug(16, "Application (" + KeyboardHero.APP_TITLE + ") integrity check have failed!");
            return;
        }
        final State state = Game.getState();
        if (state.score <= 0) return;
        if (state.score > Game.getMaxScore(state.speed)) {
            Util.debug(18, "Impossible score number!");
            return;
        }
        final String author = state.songInfo.getAuthor();
        final String songTitle = (author == null || author.equals("") ? "" : author + ": ") + state.songInfo.getTitle().replace('¦', '|');
        Util.debug("TITLE:" + state.songInfo.getTitle());
        boolean wasntNameCheck = true;
        String codeline = null;
        int foundCount = 0;
        int newEntryNum = -1;
        String[] names = new String[MAX_IN_TOP];
        String[] songs = new String[MAX_IN_TOP];
        Integer[] scores = new Integer[MAX_IN_TOP];
        Float[] factors = new Float[MAX_IN_TOP];
        String[] lines = new String[MAX_IN_TOP];
        try {
            BufferedReader bufferedRdr = new BufferedReader(new FileReader('.' + KeyboardHero.APP_NAME + ".tls"));
            String line = null;
            String[] parts;
            String[] subparts;
            String[] ssubparts;
            int tlscore;
            float tlfactor;
            while ((line = bufferedRdr.readLine()) != null) {
                parts = line.split(" ", 2);
                if (parts.length == 2 && parts[1].equals(decrypt(Util.stringReverse(parts[0])))) {
                    try {
                        parts[1] = decrypt(parts[1]);
                        subparts = parts[1].split(" ", 3);
                        if (subparts.length != 3) {
                            Util.debug(28, "Not enough subentry in toplist file: ." + KeyboardHero.APP_NAME + ".tls!");
                            continue;
                        }
                        tlscore = Integer.parseInt(subparts[1]);
                        if (wasntNameCheck && state.score > tlscore) {
                            if (Util.getPlayerName()) {
                                return;
                            }
                            wasntNameCheck = false;
                            newEntryNum = foundCount;
                            scores[foundCount] = state.score;
                            factors[foundCount] = state.speed;
                            songs[foundCount] = songTitle;
                            names[foundCount] = (Util.isDebugMode() ? "[Debugger] " + Util.getProp("name") : Util.getProp("name"));
                            codeline = encrypt(state.speed + " " + state.score + " " + songs[foundCount] + "¦" + names[foundCount]);
                            lines[foundCount] = (codeline = Util.stringReverse(encrypt(codeline)) + " " + codeline);
                            if ((++foundCount) >= MAX_IN_TOP) break;
                        }
                        tlfactor = Float.parseFloat(subparts[0]);
                        ssubparts = subparts[2].split("¦", 2);
                        if (ssubparts.length != 2) {
                            Util.debug(26, "Not enough subsubentry in toplist file: ." + KeyboardHero.APP_NAME + ".tls!");
                            continue;
                        }
                        scores[foundCount] = tlscore;
                        factors[foundCount] = tlfactor;
                        songs[foundCount] = ssubparts[0];
                        names[foundCount] = ssubparts[1];
                        lines[foundCount] = line;
                        if ((++foundCount) >= MAX_IN_TOP) break;
                    } catch (NumberFormatException e) {
                        Util.debug(24, "Corrupted toplist score and/or level number in toplist file: ." + KeyboardHero.APP_NAME + ".tls!");
                    }
                } else {
                    Util.debug(32, "Corrupted toplist entry in toplist file: ." + KeyboardHero.APP_NAME + ".tls!");
                }
            }
            bufferedRdr.close();
            if (wasntNameCheck && foundCount < MAX_IN_TOP) {
                if (Util.getPlayerName()) {
                    return;
                }
                wasntNameCheck = false;
                newEntryNum = foundCount;
                scores[foundCount] = state.score;
                factors[foundCount] = state.speed;
                songs[foundCount] = songTitle;
                names[foundCount] = (Util.isDebugMode() ? "[Debugger] " + Util.getProp("name") : Util.getProp("name"));
                codeline = encrypt(state.speed + " " + state.score + " " + songs[foundCount] + "¦" + names[foundCount]);
                lines[foundCount++] = (codeline = Util.stringReverse(encrypt(codeline)) + " " + codeline);
            }
            writeToplist(lines, foundCount);
        } catch (Exception e) {
            Util.debug(64, "Couldn't read the toplist file: ." + KeyboardHero.APP_NAME + ".tls!");
            if (Util.getPlayerName()) {
                return;
            }
            wasntNameCheck = false;
            newEntryNum = foundCount;
            scores[foundCount] = state.score;
            factors[foundCount] = state.speed;
            songs[foundCount] = songTitle;
            names[foundCount] = (Util.isDebugMode() ? "[Debugger] " + Util.getProp("name") : Util.getProp("name"));
            codeline = encrypt(state.speed + " " + state.score + " " + songs[foundCount] + "¦" + names[foundCount]);
            lines[foundCount++] = (codeline = Util.stringReverse(encrypt(codeline)) + " " + codeline);
            writeToplist(lines, foundCount);
        }
        if (Util.getPropBool("connToplist")) {
            if (wasntNameCheck) {
                if (Util.getPlayerName()) {
                    return;
                }
            }
            if (wasntNameCheck) {
                codeline = encrypt(state.speed + " " + state.score + " " + songTitle + "¦" + (Util.isDebugMode() ? "[Debugger] " + Util.getProp("name") : Util.getProp("name")));
                codeline = Util.stringReverse(encrypt(codeline)) + " " + codeline;
            }
            final String finalCodeline = codeline;
            (new Thread() {

                public void run() {
                    try {
                        URL url = new URL(URL_STR);
                        URLConnection connection = url.openConnection(getProxy());
                        connection.setRequestProperty("User-Agent", USER_AGENT);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                        output.writeBytes("app=" + URLEncoder.encode(KeyboardHero.APP_TITLE, "UTF-8") + "&req=add&code=" + URLEncoder.encode(finalCodeline, "UTF-8"));
                        output.flush();
                        output.close();
                        DataInputStream input = new DataInputStream(connection.getInputStream());
                        input.close();
                    } catch (Exception e) {
                        Util.error(Util.getMsg("CannotToplist"), Util.getMsg("CannotToplist2"));
                    }
                }
            }).start();
        }
        if (!wasntNameCheck) {
            ((DialogToplist) KeyboardHero.getDialogs().get("toplist")).open(Util.getMsgMnemonic("Menu_LocalToplist"), names, scores, songs, factors, foundCount, newEntryNum);
        }
    }
