        public void run() {
            GlobalProperties.logger.log(rtjdds.util.Logger.INFO, getClass(), "writeSubmessage()", "Start serializing...");
            GlobalProperties.logger.printMemStats();
            Submessage submsg = null;
            switch(_submessageKind) {
                case DATA.value:
                    submsg = new Data(_readerId, _writerId, getSN(), _prefix, _suffix, _status, _parms, _user_data);
                    break;
                case NOKEY_DATA.value:
                    submsg = new NoKeyData(_readerId, _writerId, getSN(), _parms, _user_data);
                    break;
                case INFO_TS.value:
                    submsg = InfoTimestamp.now();
                    break;
                default:
                    return;
            }
            submsg.write(_packet);
            GlobalProperties.logger.log(rtjdds.util.Logger.INFO, getClass(), "writeSubmessage()", "Ended serializing...");
            GlobalProperties.logger.printMemStats();
        }
