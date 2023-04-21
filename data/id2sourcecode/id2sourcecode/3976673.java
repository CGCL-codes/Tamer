        public void send() {
            try {
                Transport.send(this.getMessage());
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
        }
