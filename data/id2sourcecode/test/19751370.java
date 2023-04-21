    public IMessage processReceivedSyncMessage(IMessage msg) {
        switch(msg.getSignal()) {
            case ISignal.ACTION_COPY_TOCLIPBOARD:
                if (msg.getData() instanceof String) {
                    writeStringToClipboard((String) msg.getData());
                } else {
                    copyToClipboard((IXholon) msg.getData());
                }
                break;
            case ISignal.ACTION_CUT_TOCLIPBOARD:
                cutToClipboard((IXholon) msg.getData());
                break;
            case ISignal.ACTION_PASTE_LASTCHILD_FROMCLIPBOARD:
                pasteLastChildFromClipboard((IXholon) msg.getData());
                break;
            case ISignal.ACTION_PASTE_FIRSTCHILD_FROMCLIPBOARD:
                pasteFirstChildFromClipboard((IXholon) msg.getData());
                break;
            case ISignal.ACTION_PASTE_AFTER_FROMCLIPBOARD:
                pasteAfterFromClipboard((IXholon) msg.getData());
                break;
            case ISignal.ACTION_PASTE_BEFORE_FROMCLIPBOARD:
                pasteBeforeFromClipboard((IXholon) msg.getData());
                break;
            case ISignal.ACTION_PASTE_REPLACEMENT_FROMCLIPBOARD:
                pasteReplacementFromClipboard((IXholon) msg.getData());
                break;
            case ISignal.ACTION_XQUERY_THRUCLIPBOARD:
                writeStringToClipboard(doXQuery((IXholon) msg.getData(), readStringFromClipboard(), IXholon2Xml.XHATTR_TO_NULL, false, false));
                break;
            case ISignal.ACTION_PASTE_MERGE_FROMCLIPBOARD:
                pasteMergeFromClipboard((IXholon) msg.getData());
                break;
            case ISignal.ACTION_START_XQUERY_GUI:
                xqueryGui((IXholon) msg.getData());
                break;
            case ISignal.ACTION_START_XHOLON_CONSOLE:
                xholonConsole((IXholon) msg.getData());
                break;
            case ISignal.ACTION_START_WEB_BROWSER:
                webBrowser((IXholon) msg.getData());
                break;
            case ISignal.ACTION_PASTE_LASTCHILD_FROMSTRING:
                pasteLastChild(msg.getSender(), (String) msg.getData());
                break;
            case ISignal.ACTION_PASTE_FIRSTCHILD_FROMSTRING:
                pasteFirstChild(msg.getSender(), (String) msg.getData());
                break;
            case ISignal.ACTION_PASTE_AFTER_FROMSTRING:
                pasteAfter(msg.getSender(), (String) msg.getData());
                break;
            case ISignal.ACTION_PASTE_BEFORE_FROMSTRING:
                pasteBefore(msg.getSender(), (String) msg.getData());
                break;
            case ISignal.ACTION_PASTE_REPLACEMENT_FROMSTRING:
                pasteReplacement(msg.getSender(), (String) msg.getData());
                break;
            case ISignal.ACTION_PASTE_MERGE_FROMSTRING:
                pasteMerge(msg.getSender(), (String) msg.getData());
                break;
            case ISignal.ACTION_PASTE_LASTCHILD_FROMDROP:
                pasteLastChildFromDrop(msg.getSender(), (String) msg.getData());
                break;
            case ISignal.ACTION_PASTE_FIRSTCHILD_FROMDROP:
                pasteFirstChildFromDrop(msg.getSender(), (String) msg.getData());
                break;
            case ISignal.ACTION_PASTE_AFTER_FROMDROP:
                pasteAfterFromDrop(msg.getSender(), (String) msg.getData());
                break;
            default:
                return super.processReceivedSyncMessage(msg);
        }
        return new Message(SIG_XHOLON_HELPER_RESP, null, this, msg.getSender());
    }
