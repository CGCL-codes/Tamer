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
