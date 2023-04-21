    public static void write(org.omg.CORBA.portable.OutputStream ostream, com.sun.corba.se.spi.activation.ServerAlreadyInstalled value) {
        ostream.write_string(id());
        ostream.write_long(value.serverId);
    }
