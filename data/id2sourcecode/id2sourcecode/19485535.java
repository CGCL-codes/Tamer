        private void handle() throws IOException, NullPointerException {
            Vector<IOListener> analog = new Vector<IOListener>(), digital = new Vector<IOListener>(), counter = new Vector<IOListener>();
            for (final IOListener l : listenerList) {
                switch(l.getDataType()) {
                    case ANALOG:
                        analog.add(copy(l));
                        break;
                    case COUNTER:
                        counter.add(copy(l));
                        break;
                    case DIGITAL:
                        digital.add(copy(l));
                        break;
                }
            }
            {
                if (analog.size() != 0) {
                    short[] a = new short[2];
                    if (analog.size() == 1 & !analog.get(0).listenToAllChannels()) {
                        short channelIndex = (short) (((AnalogInput) (analog.get(0).getChannel())).channelNo - 1);
                        a[channelIndex] = jWrapper.readAnalogInput(channelIndex);
                        postEvent(new AnalogEvent(analog.get(0).getTargetComponent(), a[channelIndex], System.currentTimeMillis()));
                    } else {
                        a = jWrapper.readAllAnalogInput();
                        {
                            for (IOListener listener : analog) {
                                if (listener.listenToAllChannels()) {
                                    postEvent(new AnalogAllEvent(listener.getTargetComponent(), a, System.currentTimeMillis()));
                                } else {
                                    postEvent(new AnalogEvent(listener.getTargetComponent(), a[1], System.currentTimeMillis()));
                                }
                            }
                        }
                    }
                }
            }
            {
                if (digital.size() != 0) {
                    boolean[] newDigitalIn = new boolean[5];
                    boolean singleListenersOnly = true;
                    for (IOListener listener : digital) {
                        if (listener.listenToAllChannels()) {
                            singleListenersOnly = false;
                            break;
                        }
                    }
                    if (digital.size() < 3 & singleListenersOnly) {
                        for (IOListener listener : digital) {
                            DigitalInput channel = ((DigitalInput) listener.getChannel());
                            newDigitalIn[channel.channelNo - 1] = jWrapper.readDigitalInput(channel);
                        }
                    } else {
                        newDigitalIn = jWrapper.readAllDigitalInput();
                    }
                    boolean oneOrMoreChanged = false;
                    for (int i = 0; i < 5; i++) {
                        if (newDigitalIn[i] == digitalIn[i]) {
                            oneOrMoreChanged = true;
                            break;
                        }
                    }
                    if (oneOrMoreChanged) {
                        for (IOListener listener : digital) {
                            if (listener.listenToAllChannels()) {
                                postEvent(new DigitalAllEvent(listener.getTargetComponent(), newDigitalIn, System.currentTimeMillis()));
                            } else {
                                int channelNo = ((DigitalInput) listener.getChannel()).channelNo;
                                if (newDigitalIn[channelNo - 1] != digitalIn[channelNo - 1]) {
                                    postEvent(new DigitalEvent(listener.getTargetComponent(), newDigitalIn[channelNo - 1], System.currentTimeMillis()));
                                }
                            }
                        }
                    }
                    digitalIn = newDigitalIn;
                }
            }
            {
                if (counter.size() != 0) {
                    long[] c = new long[2];
                    if (counter.size() == 1 & !counter.get(0).listenToAllChannels()) {
                        int channelIndex = ((Counter) counter.get(0).getChannel()).channelNo;
                        c[channelIndex - 1] = jWrapper.readCounter(Counter.getChannelForIndex(channelIndex));
                        if (c[channelIndex - 1] != counterIn[channelIndex - 1]) {
                            counterIn[channelIndex - 1] = c[channelIndex - 1];
                            postEvent(new CounterEvent(counter.get(0).getTargetComponent(), c[channelIndex - 1], System.currentTimeMillis()));
                        }
                    } else {
                        c[0] = jWrapper.readCounter(Counter.CHANNEL1);
                        c[1] = jWrapper.readCounter(Counter.CHANNEL2);
                        if (c[0] == counterIn[0]) {
                            if (c[1] == counterIn[1]) {
                            } else {
                                for (IOListener listener : counter) {
                                    if (listener.listenToAllChannels()) {
                                        postEvent(new CounterAllEvent(listener.getTargetComponent(), c, System.currentTimeMillis()));
                                    } else {
                                        if (((Counter) listener.getChannel()).channelNo == 2) {
                                            postEvent(new CounterEvent(listener.getTargetComponent(), c[1], System.currentTimeMillis()));
                                        }
                                    }
                                }
                            }
                        } else {
                            if (c[1] == counterIn[1]) {
                                for (IOListener listener : counter) {
                                    if (listener.listenToAllChannels()) {
                                        postEvent(new CounterAllEvent(listener.getTargetComponent(), c, System.currentTimeMillis()));
                                    } else {
                                        if (((Counter) listener.getChannel()).channelNo == 1) {
                                            postEvent(new CounterEvent(listener.getTargetComponent(), c[0], System.currentTimeMillis()));
                                        }
                                    }
                                }
                            } else {
                                for (IOListener listener : counter) {
                                    if (listener.listenToAllChannels()) {
                                        postEvent(new CounterAllEvent(listener.getTargetComponent(), c, System.currentTimeMillis()));
                                    } else {
                                        postEvent(new CounterEvent(listener.getTargetComponent(), c[((Counter) listener.getChannel()).channelNo - 1], System.currentTimeMillis()));
                                    }
                                }
                            }
                        }
                        counterIn = c;
                    }
                }
            }
            synchronized (outputChanged) {
                if (!outputChanged) return;
                outputChanged = false;
            }
            {
                boolean value1Changed = (analogOut[0] != analogOut_OLD[0]);
                boolean value2Changed = (analogOut[1] != analogOut_OLD[1]);
                if (value1Changed) {
                    if (value2Changed) {
                        analogOut_OLD[0] = analogOut[0];
                        analogOut_OLD[1] = analogOut[1];
                        jWrapper.setAllAnalogOutput(analogOut[0], analogOut[1]);
                    } else {
                        analogOut_OLD[0] = analogOut[0];
                        jWrapper.setAnalogOutput((short) 1, analogOut[0]);
                    }
                } else if (value2Changed) {
                    analogOut_OLD[1] = analogOut[1];
                    jWrapper.setAnalogOutput((short) 2, analogOut[1]);
                }
            }
            {
                int channelsChangedCount = 0;
                for (int i = 0; i < 8; i++) {
                    if (digitalOut[i] != digitalOut_OLD[i]) channelsChangedCount++;
                }
                if (channelsChangedCount == 1) {
                    for (short i = 0; i < 8; i++) {
                        if (digitalOut[i] != digitalOut_OLD[i]) {
                            jWrapper.setDigitalOutput(i + 1, digitalOut[i]);
                            digitalOut_OLD[i] = digitalOut[i];
                        }
                    }
                } else if (channelsChangedCount > 1) {
                    jWrapper.setAllDigitalOutput(digitalOut);
                    for (int i = 0; i < 8; i++) {
                        digitalOut_OLD[i] = digitalOut[i];
                    }
                }
            }
            {
                if (counterDebounce[0] != counterDebounce_OLD[0]) {
                    counterDebounce_OLD[0] = counterDebounce[0];
                    jWrapper.setCounterDebounceTime(Counter.CHANNEL1, counterDebounce[0]);
                }
                if (counterDebounce[1] != counterDebounce_OLD[1]) {
                    counterDebounce_OLD[1] = counterDebounce[1];
                    jWrapper.setCounterDebounceTime(Counter.CHANNEL2, counterDebounce[1]);
                }
                boolean reset;
                reset = resetCounter1;
                synchronized (resetCounter1) {
                    resetCounter1 = false;
                }
                if (reset) {
                    jWrapper.resetCounter(Counter.CHANNEL1);
                }
                reset = resetCounter2;
                synchronized (resetCounter2) {
                    resetCounter2 = false;
                }
                if (reset) {
                    jWrapper.resetCounter(Counter.CHANNEL2);
                }
            }
        }
