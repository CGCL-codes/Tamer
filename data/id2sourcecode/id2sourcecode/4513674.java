    public static void write(org.omg.CORBA.portable.OutputStream ostream, com.sun.corba.se.spi.activation.ServerAlreadyActive value) {
        ostream.write_string(id());
        ostream.write_long(value.serverId);
    }
