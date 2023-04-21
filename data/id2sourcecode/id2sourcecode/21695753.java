        private static void sampleReply(ConversationBlip blip) {
            write(blip);
            ConversationThread thread = blip.addReplyThread(8);
            write(thread.appendBlip());
        }
