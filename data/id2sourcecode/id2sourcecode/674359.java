    public static void main(String[] args) {
        ModbusTCPMaster modbus;
        String host;
        String function;
        int port;
        int offset;
        int length;
        int[] results;
        boolean retval;
        int[] values;
        if (args.length != 5) {
            System.out.println("usage: java Master function host port offset length");
            System.out.println("function: read_output | read_input | write_output");
            return;
        }
        function = args[0];
        host = args[1];
        port = Integer.parseInt(args[2]);
        offset = Integer.parseInt(args[3]);
        length = Integer.parseInt(args[4]);
        results = new int[length];
        if (!function.equals("read_output") && !function.equals("read_input") && !function.equals("write_output")) {
            System.out.println("Invalid function!");
            return;
        }
        try {
            modbus = new ModbusTCPMaster(host, port);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }
        values = new int[100];
        for (int i = 0; i < 100; i++) {
            values[i] = i;
        }
        try {
            System.out.println("About to send request....");
            if (function.equals("read_output")) {
                retval = modbus.readMultipleRegisters(0, offset, length, 0, results);
            } else if (function.equals("read_input")) {
                retval = modbus.readInputRegisters(0, offset, length, 0, results);
            } else {
                retval = modbus.writeMultipleRegisters(0, offset, length, 0, values);
            }
            System.out.println("Send function returned");
            if (retval) {
                System.out.println("Transaction sucedded");
                if (function.equals("read_output") || function.equals("read_input")) {
                    for (int i = 0; i < length; i++) {
                        System.out.println(Integer.toHexString(results[i]));
                    }
                }
            } else {
                System.out.println("Transaction failed");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
