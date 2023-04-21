    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "_get_inaccuracy", "_get_tdf", "_get_time", "_get_utc_time", "absolute_time", "compare_time", "interval", "time_to_interval" };
        int _ob_left = 0;
        int _ob_right = _ob_names.length;
        int _ob_index = -1;
        while (_ob_left < _ob_right) {
            int _ob_m = (_ob_left + _ob_right) / 2;
            int _ob_res = _ob_names[_ob_m].compareTo(opName);
            if (_ob_res == 0) {
                _ob_index = _ob_m;
                break;
            } else if (_ob_res > 0) _ob_right = _ob_m; else _ob_left = _ob_m + 1;
        }
        switch(_ob_index) {
            case 0:
                return _OB_att_get_inaccuracy(in, handler);
            case 1:
                return _OB_att_get_tdf(in, handler);
            case 2:
                return _OB_att_get_time(in, handler);
            case 3:
                return _OB_att_get_utc_time(in, handler);
            case 4:
                return _OB_op_absolute_time(in, handler);
            case 5:
                return _OB_op_compare_time(in, handler);
            case 6:
                return _OB_op_interval(in, handler);
            case 7:
                return _OB_op_time_to_interval(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }
