    public static void usage() {
        System.out.println("ModbusTCPDevice --ip <ip> [--port <port>] [--offset <register>] [[--data <to write>][--read <length in bytes>] ... ");
        System.out.println("<ip> ip to connect to");
        System.out.println("<port> to connect to, bar code readers use next one");
        System.out.println("<offset> offset into plc normally starts at 40000");
        System.out.println("<data> what to write");
        System.out.println("<read> how many byes to read");
    }
