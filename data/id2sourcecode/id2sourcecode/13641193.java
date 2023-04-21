        protected void setProcessed(SelectableChannel channel) {
            synchronized (canceled) {
                for (CanceledEntry e : canceled) {
                    if (e.getChannel().equals(channel)) {
                        e.setProcessed();
                    }
                }
            }
        }
