    private void writeThread() {
        try {
            final BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(this.getConnection().getSocket().getOutputStream(), "UTF-8"));
            String msg;
            while (this.getConnection().getSocket().isConnected()) {
                if (!this.getBuffer().isEmpty()) {
                    msg = (String) this.getBuffer().remove();
                    System.out.println("DEBUG: " + msg);
                    wr.write(msg + "\r\n");
                    wr.flush();
                }
                if (this.getBuffer().size() <= 2 || this.getBuffer().get().toString().startsWith("WHO ")) Thread.sleep(50); else if (this.getBuffer().size() <= 5) Thread.sleep(1000); else if (this.getBuffer().size() > 5) Thread.sleep(1500);
            }
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
