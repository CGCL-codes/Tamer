        public void run() {
            try {
                uconn = url.openConnection();
                open();
            } catch (MalformedURLException mfe) {
                System.out.println(".. malformed url exception in run() ..");
            } catch (UnknownHostException uhe) {
                System.out.println(" .. unknown host in run() ..");
            } catch (IOException io) {
                System.out.println(" .. ioexception in run() ..");
            }
        }
