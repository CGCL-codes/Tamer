    public void icsEventDispatched(ICSEvent evt) {
        ICSChannelEvent chEvt = (ICSChannelEvent) evt;
        String prefix = null;
        if (showTimestamp) {
            System.out.print(BOLD_BLACK + getTimestampAsString(evt.getTimestamp()) + PLAIN);
        }
        System.out.print("<" + evt.getEventType() + ">");
        switch(chEvt.getChannel()) {
            case 1:
                System.out.print(BOLD_BLACK);
                System.out.print("[help]");
                if (chEvt.getAccountType().is(ICSAccountType.ADMIN)) System.out.print(BOLD_YELLOW); else if (chEvt.getAccountType().is(ICSAccountType.SERVICE_REP)) System.out.print(BOLD_RED); else System.out.print(PLAIN);
                System.out.print(chEvt.getPlayer());
                System.out.print(BOLD_BLACK);
                System.out.print(": ");
                System.out.print(CYAN);
                System.out.print(chEvt.getMessage());
                System.out.println(PLAIN);
                break;
            default:
                System.out.println("subscribed to unhandled channel [" + chEvt.getChannel() + "]");
        }
        System.out.flush();
    }
