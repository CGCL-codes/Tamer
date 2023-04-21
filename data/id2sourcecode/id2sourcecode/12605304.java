    public void script() {
        try {
            clientManager.connect(host, Integer.parseInt(port), tcp);
            clientManager.login(username, password);
            readMessagesAndShoutThem();
            clientManager.logout();
            System.exit(0);
        } catch (SocketException e) {
            System.err.println("Socket Exception");
            Runtime.getRuntime().halt(1);
        } catch (ariannexpTimeoutException e) {
            System.err.println("Cannot connect to Midhedava server. Server is down?");
            Runtime.getRuntime().halt(1);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace(System.err);
            Runtime.getRuntime().halt(1);
        }
    }
