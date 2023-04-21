    public void icsEventDispatched(ICSEvent evt) {
        String prefix = null;
        switch(evt.getEventType()) {
            case ICSEvent.CHANNEL_EVENT:
                switch(((ICSChannelEvent) evt).getChannel()) {
                    case 1:
                        prefix = ESC + CYAN;
                        break;
                    case 85:
                    case 88:
                        prefix = ESC + YELLOW;
                        break;
                    default:
                        prefix = ESC + BOLD_CYAN;
                }
                break;
            case ICSEvent.SHOUT_EVENT:
                switch(((ICSChannelEvent) evt).getChannel()) {
                    case ICSChannelEvent.EMOTE_CHANNEL:
                    case ICSChannelEvent.SHOUT_CHANNEL:
                        prefix = ESC + GREEN;
                        break;
                    case ICSChannelEvent.SSHOUT_CHANNEL:
                    case ICSChannelEvent.CSHOUT_CHANNEL:
                    case ICSChannelEvent.TSHOUT_CHANNEL:
                        prefix = ESC + BOLD_GREEN;
                }
                break;
            case ICSEvent.TELL_EVENT:
            case ICSEvent.SAY_EVENT:
            case ICSEvent.BOARD_SAY_EVENT:
                prefix = ESC + BOLD_YELLOW;
                break;
            case ICSEvent.KIBITZ_EVENT:
                prefix = ESC + BOLD_MAGENTA;
                break;
            case ICSEvent.WHISPER_EVENT:
                prefix = ESC + MAGENTA;
                break;
            case ICSEvent.SEEK_REMOVE_EVENT:
            case ICSEvent.SEEK_CLEAR_EVENT:
            case ICSEvent.SEEK_AD_EVENT:
            case ICSEvent.PLAYER_CONNECTION_EVENT:
            case ICSEvent.PLAYER_NOTIFICATION_EVENT:
            case ICSEvent.GAME_RESULT_EVENT:
            case ICSEvent.GAME_CREATED_EVENT:
                prefix = ESC + BOLD_BLACK;
                break;
            case ICSEvent.SEEK_AD_READABLE_EVENT:
            case ICSEvent.GAME_NOTIFICATION_EVENT:
                prefix = ESC + BLUE;
                break;
            case ICSEvent.BOARD_UPDATE_EVENT:
            case ICSEvent.MOVE_LIST_EVENT:
                prefix = ESC + YELLOW;
                break;
            default:
            case ICSEvent.CHALLENGE_EVENT:
                prefix = ESC + BOLD_RED;
                break;
        }
        if (showTimestamp) {
            System.out.print(ESC + BOLD_BLACK + getTimestampAsString(evt.getTimestamp()) + ESC + PLAIN);
        }
        if (debug) System.out.print("<" + evt.getEventType() + ">");
        if (prefix != null) System.out.println(prefix + evt + ESC + PLAIN); else System.out.println(evt);
        System.out.flush();
    }
