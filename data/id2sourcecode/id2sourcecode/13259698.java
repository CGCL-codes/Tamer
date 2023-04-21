            @Override
            public void safeRun() {
                ReadCompletion readCompletion = readCompletions.remove(key);
                if (readCompletion != null) {
                    LOG.error("Could not write  request for reading entry: " + key.entryId + " ledger-id: " + key.ledgerId + " bookie: " + channel.getRemoteAddress());
                    readCompletion.cb.readEntryComplete(BKException.Code.BookieHandleNotAvailableException, key.ledgerId, key.entryId, null, readCompletion.ctx);
                }
            }
