    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "_get_coas_version", "_get_naming_service", "_get_pid_service", "_get_terminology_service", "_get_trader_service", "are_iterators_supported", "get_components", "get_current_time", "get_default_policies", "get_observation_mgr", "get_supported_codes", "get_supported_policies", "get_supported_qualifiers", "get_type_code_for_observation_type" };
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
                return _OB_att_get_coas_version(in, handler);
            case 1:
                return _OB_att_get_naming_service(in, handler);
            case 2:
                return _OB_att_get_pid_service(in, handler);
            case 3:
                return _OB_att_get_terminology_service(in, handler);
            case 4:
                return _OB_att_get_trader_service(in, handler);
            case 5:
                return _OB_op_are_iterators_supported(in, handler);
            case 6:
                return _OB_op_get_components(in, handler);
            case 7:
                return _OB_op_get_current_time(in, handler);
            case 8:
                return _OB_op_get_default_policies(in, handler);
            case 9:
                return _OB_op_get_observation_mgr(in, handler);
            case 10:
                return _OB_op_get_supported_codes(in, handler);
            case 11:
                return _OB_op_get_supported_policies(in, handler);
            case 12:
                return _OB_op_get_supported_qualifiers(in, handler);
            case 13:
                return _OB_op_get_type_code_for_observation_type(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }
