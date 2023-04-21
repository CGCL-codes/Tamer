    public String authUser(String nick, String room) {
        String sessId = null;
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            if (!r.existNick(nick) && nick.length() < 21 && !r.isFull() && nick.charAt(0) != ' ') {
                long system = System.currentTimeMillis();
                byte[] time = new Long(system).toString().getBytes();
                try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.update(time);
                    sessId = toHex(md5.digest());
                } catch (Exception e) {
                    System.err.println("Unable to calculate MD5 Digests.");
                    System.exit(0);
                }
                User u = new User(nick, room, sessId);
                r.addWaitingUser(u);
                return sessId;
            }
        }
        return sessId;
    }
