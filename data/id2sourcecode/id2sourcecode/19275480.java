        public void run() {
            int playIndex = 0;
            int wordIndex = 0;
            int wordStart = 0;
            int phonemeIndex = 0;
            double timeNextPhone = 0;
            long nextTimeStamp = 0;
            final byte[] buffer = new byte[BUFFER_LENGTH];
            while (!done) {
                final QueueItem item = getNextQueueItem();
                final Object source = item.getSource();
                final int id = item.getId();
                final SpeakableListener listener = item.getListener();
                postTopOfQueue(item);
                try {
                    delayUntilResumed(item);
                } catch (InterruptedException e1) {
                    return;
                }
                long totalBytesRead = 0;
                final SpeakableEvent startedEvent = new SpeakableEvent(source, SpeakableEvent.SPEAKABLE_STARTED, id);
                synthesizer.postSpeakableEvent(startedEvent, listener);
                playIndex = 0;
                wordIndex = 0;
                wordStart = 0;
                phonemeIndex = 0;
                timeNextPhone = 0;
                int bytesRead = 0;
                final BaseAudioManager manager = (BaseAudioManager) synthesizer.getAudioManager();
                final AudioFormat format;
                try {
                    format = manager.getAudioFormat();
                } catch (AudioException e) {
                    e.printStackTrace();
                    break;
                }
                final float sampleRate = format.getSampleRate();
                long bps = format.getChannels();
                bps *= sampleRate;
                bps *= (format.getSampleSizeInBits() / 8);
                try {
                    while (true) {
                        final AudioSegment segment = item.getAudioSegment();
                        if ((segment == null) || !segment.isGettable()) {
                            throw new SecurityException("The platform does not allow to access the input " + "stream!");
                        }
                        final InputStream inputStream = segment.openInputStream();
                        if (inputStream == null) {
                            break;
                        }
                        bytesRead = inputStream.read(buffer);
                        if (bytesRead < 0) {
                            break;
                        }
                        totalBytesRead += bytesRead;
                        synchronized (cancelLock) {
                            if (cancelFirstItem) {
                                final SpeakableEvent cancelledEvent = new SpeakableEvent(source, SpeakableEvent.SPEAKABLE_CANCELLED, id);
                                synthesizer.postSpeakableEvent(cancelledEvent, listener);
                                break;
                            }
                        }
                        try {
                            delayUntilResumed(item);
                        } catch (InterruptedException e) {
                            return;
                        }
                        synchronized (cancelLock) {
                            if (cancelFirstItem) {
                                synthesizer.postSpeakableEvent(new SpeakableEvent(source, SpeakableEvent.SPEAKABLE_CANCELLED, id), listener);
                                break;
                            }
                        }
                        while (wordIndex < item.getWords().length && (item.getWordsStartTime()[wordIndex] * sampleRate <= playIndex * bytesRead)) {
                            synthesizer.postSpeakableEvent(new SpeakableEvent(source, SpeakableEvent.WORD_STARTED, id, item.getWords()[wordIndex], wordStart, wordStart + item.getWords()[wordIndex].length()), listener);
                            wordStart += item.getWords()[wordIndex].length() + 1;
                            wordIndex++;
                        }
                        while (phonemeIndex < item.getPhonesInfo().length && timeNextPhone * sampleRate < playIndex * bytesRead) {
                            synthesizer.postSpeakableEvent(new SpeakableEvent(source, SpeakableEvent.PHONEME_STARTED, id, item.getWords()[wordIndex - 1], item.getPhonesInfo(), phonemeIndex), listener);
                            timeNextPhone += (double) item.getPhonesInfo()[phonemeIndex].getDuration() / (double) 1000;
                            phonemeIndex++;
                        }
                        playIndex++;
                        final OutputStream out = manager.getOutputStream();
                        out.write(buffer, 0, bytesRead);
                        long dataTime = (long) (1000 * bytesRead / bps);
                        final long system = System.currentTimeMillis();
                        if (nextTimeStamp - system < -dataTime) {
                            nextTimeStamp = system + dataTime;
                        } else {
                            nextTimeStamp += dataTime;
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
                try {
                    final OutputStream out = manager.getOutputStream();
                    if (out != null) {
                        out.flush();
                    }
                } catch (IOException ex) {
                    return;
                }
                if (!cancelFirstItem) {
                    synthesizer.postSpeakableEvent(new SpeakableEvent(source, SpeakableEvent.SPEAKABLE_ENDED, id), listener);
                    synchronized (playQueue) {
                        playQueue.removeElement(item);
                    }
                }
                synchronized (cancelLock) {
                    cancelFirstItem = false;
                }
                postEventsAfterPlay();
            }
        }
