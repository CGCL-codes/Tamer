        int getPersistChanGrpMgrIdx() {
            int result = -1;
            for (int i = 0; i < managers.length; i++) {
                if (managers[i].hasChannel((Channel) theItem.getChannel())) {
                    assertTrue("Same channel in two groups", result == -1);
                    result = i;
                }
            }
            assertTrue("Channel not found in any group", result != -1);
            return result;
        }
