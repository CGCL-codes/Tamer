            public void actionPerformed(ActionEvent e) {
                WrappedChannel wchI = wrpV.get(0);
                WrappedChannel wchB = wrpV.get(3);
                WrappedChannel wchB_Book = wrpV.get(6);
                if (wchB.isGood()) {
                    double coeff = wchB.getValue() / wchI.getValue();
                    double valI = wchI.getValue() * injSpt_coeff_TextField.getValue();
                    double valB_Book = coeff * valI;
                    wchI.setValue(valI);
                    wchB_Book.setValue(valB_Book);
                } else {
                    messageTextLocal.setText("Cannot read PV: " + wchB.getChannelName());
                }
                wchI = wrpV.get(1);
                wchB = wrpV.get(4);
                wchB_Book = wrpV.get(7);
                if (wchB.isGood()) {
                    double coeff = wchB.getValue() / wchI.getValue();
                    double valI = wchI.getValue() * dha11_coeff_TextField.getValue();
                    double valB_Book = coeff * valI;
                    wchI.setValue(valI);
                    wchB_Book.setValue(valB_Book);
                } else {
                    messageTextLocal.setText("Cannot read PV: " + wchB.getChannelName());
                }
                wchI = wrpV.get(2);
                wchB = wrpV.get(5);
                wchB_Book = wrpV.get(8);
                if (wchB.isGood()) {
                    double coeff = wchB.getValue() / wchI.getValue();
                    double valI = dha12_coeff_TextField.getValue();
                    double valB_Book = coeff * valI;
                    wchI.setValue(valI);
                    wchB_Book.setValue(valB_Book);
                } else {
                    messageTextLocal.setText("Cannot read PV: " + wchB.getChannelName());
                }
            }
