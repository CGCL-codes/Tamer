        private static void biggerSampleReply(ConversationBlip blip) {
            write(blip);
            ConversationThread thread = blip.addReplyThread();
            sampleReply(thread.appendBlip());
            sampleReply(thread.appendBlip());
            write(thread.appendBlip());
        }
