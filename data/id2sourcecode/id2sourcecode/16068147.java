        private void createNewGameWindow(final Settings settings, final Player[] players, final EncryptedMessageReader reader, final EncryptedMessageWriter writer) {
            invokeLater(new Runnable() {

                public void run() {
                    new GameWindow(settings, players, reader, writer);
                }
            });
        }
