    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.mndacs.corba.ServerSetC value) {
        ostream.write_long(value.CPU_count);
        ostream.write_double(value.CPU_usage);
        ostream.write_long(value.CuedJobs);
        ostream.write_string(value.HostID);
        ostream.write_string(value.IP);
        ostream.write_long(value.JobThreads);
        ostream.write_long(value.KnownServer);
        ostream.write_long(value.LoadedDAL);
        ostream.write_long(value.LoadedNodes);
        ostream.write_longlong(value.MEM_max);
        ostream.write_double(value.MEM_usage);
        ostream.write_long(value.SysComPort);
        ostream.write_long(value.connection_rejected_count);
        ostream.write_string(value.CORBA_Command_Ref);
        ostream.write_string(value.CORBA_ServerInfo_Ref);
        ostream.write_string(value.CORBA_DAL_Ref);
    }
