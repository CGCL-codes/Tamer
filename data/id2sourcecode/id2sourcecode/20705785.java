        public void setDataForDay(int day) {
            if (m_programs != null) {
                Channels channels = Programs.getChannels();
                if (channels != null) {
                    m_cellData = new TVGridCellData[m_rowCount][m_colCount];
                    ProgramList progs = m_programs.getProgramsSortedByTime();
                    Iterator it = progs.iterator();
                    String curDate = "###";
                    int curDay = 0;
                    TVGridCellData cell;
                    while (it.hasNext()) {
                        ProgItem prog = (ProgItem) it.next();
                        String start = m_programs.getStartTime(prog);
                        if (start.indexOf(curDate) != 0) {
                            curDate = start.substring(0, 8);
                            curDay++;
                        }
                        if (curDay == day) {
                            int channelIndex = m_channelDescs.indexOf(Programs.getChannelDesc(m_programs.getChannel(prog)));
                            if (channelIndex != -1) {
                                int hour = Integer.valueOf(start.substring(8, 10)).intValue();
                                int min = Integer.valueOf(start.substring(10, 12)).intValue();
                                int hourIndex = hour * 2;
                                if (min >= 30) hourIndex++;
                                cell = new TVGridCellData();
                                cell.prog = prog;
                                String end = m_programs.getStopTime(prog);
                                int endHourIndex = 0;
                                try {
                                    int endhour = Integer.valueOf(end.substring(8, 10)).intValue();
                                    min = Integer.valueOf(end.substring(10, 12)).intValue();
                                    if (endhour < hour) endhour += 24;
                                    endHourIndex = endhour * 2;
                                    if (min >= 30) endHourIndex++;
                                } catch (Exception e) {
                                    endHourIndex = hourIndex + 1;
                                }
                                cell.colSpan = endHourIndex - hourIndex;
                                m_cellData[channelIndex][hourIndex] = cell;
                            }
                        }
                    }
                }
            }
        }
