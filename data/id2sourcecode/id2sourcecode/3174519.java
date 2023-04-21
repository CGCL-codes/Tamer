    private void handleCommand(ArrayList<String> pm, String sender, String login, String hostname) {
        if (pm.get(0).equalsIgnoreCase(CMD_PING)) {
            log("Pinged: " + sender + ":" + login + ":" + hostname);
            this.sendNotice(sender, "PONG");
        } else if (pm.get(0).equalsIgnoreCase(CMD_REVERSE)) {
            log("Reversing last message.");
            this.sendNotice(this.channel, this.reverseMessage(this.lastMessage));
        } else if (pm.size() >= 2 && (pm.get(0).equalsIgnoreCase(CMD_2V2) || pm.get(0).equalsIgnoreCase(CMD_3V3) || pm.get(0).equalsIgnoreCase(CMD_5V5))) {
            try {
                this.handleArenaRating(pm.get(0), Integer.parseInt(pm.get(1)), sender);
            } catch (NumberFormatException nfe) {
                log("Failed parsing 'rating' for function '" + pm.get(0) + "'");
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_TIME)) {
            log("Queried for the time.");
            this.sendNotice(sender, (new Date(System.currentTimeMillis())).toString());
        } else if (pm.get(0).equalsIgnoreCase(CMD_SHA1)) {
            if (pm.size() >= 2) {
                log("Returning a sha1 hash of: " + pm.get(1));
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA1");
                    md.reset();
                    byte[] digest;
                    digest = md.digest(pm.get(1).getBytes());
                    StringBuffer hexString = new StringBuffer();
                    for (int i = 0; i < digest.length; i++) {
                        hexString.append(hexDigit(digest[i]));
                    }
                    this.sendNotice(sender, hexString.toString());
                } catch (NoSuchAlgorithmException e) {
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_MD5)) {
            if (pm.size() >= 2) {
                log("Returning a md5 hash of: " + pm.get(1));
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.reset();
                    byte[] digest;
                    digest = md.digest(pm.get(1).getBytes());
                    StringBuffer hexString = new StringBuffer();
                    for (int i = 0; i < digest.length; i++) {
                        hexString.append(hexDigit(digest[i]));
                    }
                    this.sendNotice(sender, hexString.toString());
                } catch (NoSuchAlgorithmException e) {
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_RANDOM) && pm.size() == 2) {
            try {
                this.log(sender + "!" + login + "@" + hostname + " issued random for: " + pm.get(1));
                this.findRandom(sender, sender, login, hostname, pm.get(1));
            } catch (NoSuchURLException nste) {
                this.sendNotice(sender, "Sorry, nothing found for tag: " + pm.get(1));
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_RANDOM) && pm.size() == 1) {
            boolean foundOne = false;
            while (!foundOne) {
                try {
                    this.log(sender + "!" + login + "@" + hostname + " issued random with no tag.");
                    String tag = this.taghandler.getRandomTag();
                    this.findRandom(sender, sender, login, hostname, tag);
                    foundOne = true;
                } catch (NoSuchURLException nste) {
                    this.log(nste.getMessage());
                } catch (SQLException sqle) {
                }
            }
        } else if (pm.get(0).equalsIgnoreCase(CMD_QQ) && pm.size() > 1) {
            String output = "";
            for (String string : pm) {
                if (!string.equalsIgnoreCase(CMD_QQ)) output += string + " ";
            }
            this.sendNotice(sender, output.replaceAll("[A-Z]", "Q").replaceAll("[a-z]", "q"));
        } else {
            this.handlePublicCommand(pm, sender, login, hostname);
        }
    }
