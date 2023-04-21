    private boolean parseParams(String params, Pattern regex) {
        Matcher m = regex.matcher(params);
        if (m == null || !m.matches()) {
            mStream.writeErr("invalid parameters");
            return false;
        }
        String s;
        switch(m.groupCount()) {
            default:
                assert false;
                return false;
            case 2:
                s = m.group(2);
                mOtherParam = Integer.parseInt(s);
                if (mOtherParam < 0) {
                    mStream.writeErr("invalid 2nd parameter");
                    return false;
                }
            case 1:
                s = m.group(1);
                if (s == null) mMsgId = -1; else {
                    mMsgId = Integer.parseInt(s);
                    if (mMsgId < 1) {
                        mStream.writeErr("invalid msg number");
                        return false;
                    }
                    if (mMsgId > mMail.length) {
                        mStream.writeErr("no such message, only " + mMail.length + " messages in maildrop");
                        return false;
                    }
                    mMsgIndex = mMsgId - 1;
                    if (mDeletedMsgs[mMsgIndex]) {
                        mStream.writeErr("message " + mMsgId + " already deleted");
                        return false;
                    }
                }
                break;
        }
        return true;
    }
