    private void scheduleOx(final MapleMap map, final long newDelay) {
        TimerManager.getInstance().schedule(new Runnable() {

            public void run() {
                map.broadcastMessage(MaplePacketCreator.serverNotice(6, MapleOxQuizFactory.getOXQuestion(round, question)));
                TimerManager.getInstance().schedule(new Runnable() {

                    public void run() {
                        for (MapleCharacter curChar : map.getCharacters()) {
                            if (isCorrectAnswer(curChar, MapleOxQuizFactory.getOXAnswer(round, question))) {
                                curChar.gainExp(expGain * curChar.getClient().getChannelServer().getExpRate(), true, false);
                            } else {
                                curChar.setHp(0);
                                curChar.updateSingleStat(MapleStat.HP, 0);
                            }
                        }
                        scheduleAnswer(map);
                    }
                }, 15 * 1000);
            }
        }, newDelay);
    }
