        public void execute() {
            if (TDebug.TraceCdda) {
                TDebug.out("CddaAudioInputStream.execute(): begin");
            }
            if (!isEndOfTrackReached()) {
                if (TDebug.TraceCdda) {
                    TDebug.out("CddaAudioInputStream.execute(): begin");
                }
                while (getCircularBuffer().availableWrite() >= BUFFER_SIZE && !isEndOfTrackReached()) {
                    if (TDebug.TraceCdda) {
                        TDebug.out("CddaAudioInputStream.execute(): before readFrame()");
                    }
                    m_cookedIoctl.readFrame(getCurrentFrameNumber(), 1, m_abData);
                    if (TDebug.TraceCdda) {
                        TDebug.out("CddaAudioInputStream.execute(): after readFrame(), before cb.write()");
                    }
                    getCircularBuffer().write(m_abData, 0, BUFFER_SIZE);
                    if (TDebug.TraceCdda) {
                        TDebug.out("CddaAudioInputStream.execute(): after cb.write()");
                    }
                    increaseCurrentFrameNumber();
                }
            } else {
                if (TDebug.TraceCdda) {
                    TDebug.out("CddaAudioInputStream.execute(): end of cdda track");
                }
                getCircularBuffer().close();
            }
            if (TDebug.TraceCdda) {
                TDebug.out("CddaAudioInputStream.execute(): end");
            }
        }
