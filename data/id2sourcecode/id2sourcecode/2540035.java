    Win32SerialPort(String portName) {
        in = new Win32SerialInputStream(this);
        out = new Win32SerialOutputStream(this);
        handle = NativeApi.openPort("\\\\.\\" + portName, in.readComplete, out.writeComplete);
    }
