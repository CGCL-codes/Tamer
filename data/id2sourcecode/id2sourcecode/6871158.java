    public TransportHelperFilterSwitcher(TransportHelperFilter _filter1, TransportHelperFilter _filter2, int _switch_read, int _switch_write) {
        first_filter = _filter1;
        second_filter = _filter2;
        read_rem = _switch_read;
        write_rem = _switch_write;
        current_reader = read_rem <= 0 ? second_filter : first_filter;
        current_writer = write_rem <= 0 ? second_filter : first_filter;
    }
