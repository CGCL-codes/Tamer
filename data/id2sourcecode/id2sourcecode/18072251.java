        public void write() throws IOException {
            writeDWord(signature);
            writeDWord(ofsNext);
            writeDWord(ofsFirstSettings);
            writeDWord(eventCount);
            writeDWord(ofsFirstEvent);
            writeDWord(ofsLastEvent);
            writeDWord(ofsFirstUnreadEvent);
            writeDWord(timestampFirstUnread);
        }
