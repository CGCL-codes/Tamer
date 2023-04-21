                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                    }
                                    setCurrentValue(scanVariable.getValue());
                                    if (scanVariable.getChannelRB() != null) {
                                        setCurrentValueRB(scanVariable.getValueRB());
                                    } else {
                                        valueTextRB.setText(null);
                                    }
                                }
