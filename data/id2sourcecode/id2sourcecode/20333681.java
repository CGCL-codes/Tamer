        public Object getValueAt(int rowIndex, int columnIndex) {
            SinglePartyCall call = (SinglePartyCall) presentationModel.getCalls(PresentationModel.SINGLEPARTY_CALLTYPE).get(rowIndex);
            switch(columnIndex) {
                case 0:
                    return call.getId();
                case 1:
                    return call.getChannel().getDescriptor().getEndpoint().getId();
                case 2:
                    switch(call.getState()) {
                        case Call.IDLE_STATE:
                            return "IDLE";
                        case Call.CONNECTING_STATE:
                            return "CONNECTING";
                        case Call.ACTIVE_STATE:
                            return "ACTIVE";
                        case Call.INVALID_STATE:
                            return "INVALID";
                    }
                case 3:
                    return call.getReasonForStateChange();
            }
            return null;
        }
