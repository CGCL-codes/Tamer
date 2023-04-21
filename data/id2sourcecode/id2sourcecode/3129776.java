        public void run() {
            for (int i = 0; i < files.length; i++) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException f) {
                }
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    BufferedReader reader = new BufferedReader(new FileReader(files[i]));
                    int n;
                    while ((n = reader.read()) != -1) stream.write(n);
                    byte[] array = stream.toByteArray();
                    Message m = MailService.createMessage();
                    m.setRemote(false);
                    MailDecoder.decodeMail(array, m);
                    mailbox.addMessage(m);
                } catch (IOException f) {
                }
                bar.workProgress((i + 1) * 10);
            }
            label.setText("<html><body>Done!" + "<br>U can now locate the imported <br>" + "messages in the choosen folder");
        }
