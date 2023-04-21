                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                    }
                                    setParamCurrentValue(paramVariable.getValue());
                                    if (paramVariable.getChannelRB() != null) {
                                        setParamCurrentValueRB(paramVariable.getValueRB());
                                    }
                                }
