    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "_get_MyFactory", "_get_default_consumer_admin", "_get_default_filter_factory", "_get_default_supplier_admin", "destroy", "for_consumers", "for_suppliers", "get_admin", "get_all_consumeradmins", "get_all_supplieradmins", "get_consumeradmin", "get_qos", "get_supplieradmin", "new_for_consumers", "new_for_suppliers", "set_admin", "set_qos", "validate_qos" };
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
                return _OB_att_get_MyFactory(in, handler);
            case 1:
                return _OB_att_get_default_consumer_admin(in, handler);
            case 2:
                return _OB_att_get_default_filter_factory(in, handler);
            case 3:
                return _OB_att_get_default_supplier_admin(in, handler);
            case 4:
                return _OB_op_destroy(in, handler);
            case 5:
                return _OB_op_for_consumers(in, handler);
            case 6:
                return _OB_op_for_suppliers(in, handler);
            case 7:
                return _OB_op_get_admin(in, handler);
            case 8:
                return _OB_op_get_all_consumeradmins(in, handler);
            case 9:
                return _OB_op_get_all_supplieradmins(in, handler);
            case 10:
                return _OB_op_get_consumeradmin(in, handler);
            case 11:
                return _OB_op_get_qos(in, handler);
            case 12:
                return _OB_op_get_supplieradmin(in, handler);
            case 13:
                return _OB_op_new_for_consumers(in, handler);
            case 14:
                return _OB_op_new_for_suppliers(in, handler);
            case 15:
                return _OB_op_set_admin(in, handler);
            case 16:
                return _OB_op_set_qos(in, handler);
            case 17:
                return _OB_op_validate_qos(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }
