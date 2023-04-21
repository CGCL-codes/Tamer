        ItemLogEntry(Item anitem, String akey) {
            theItem = anitem;
            theKey = akey;
            channelName = anitem.getChannel().getTitle();
            recordCounter = 0;
            verifyCounter = 0;
            deleted = false;
            unread = true;
        }
