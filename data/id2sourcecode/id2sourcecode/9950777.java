    public void onMessage(PlineMessage m, List<Message> out) {
        int slot = m.getSlot();
        if (slot < 1 || slot > 6) {
            out.add(m);
            return;
        }
        String text = m.getText();
        float charsByLine = 70;
        int lineCount = (int) Math.ceil(text.length() / charsByLine);
        long now = System.currentTimeMillis();
        boolean isRateExceeded = false;
        for (int i = 0; i < lineCount; i++) {
            isRateExceeded = isRateExceeded || isRateExceeded(slot - 1, now);
        }
        if (slot > 0 && isRateExceeded) {
            if ((now - lastWarning) > warningPeriod * 1000) {
                User user = getChannel().getPlayer(slot);
                out.add(new PlineMessage("filter.flood.blocked", user.getName()));
                lastWarning = now;
            }
        } else {
            out.add(m);
        }
    }
