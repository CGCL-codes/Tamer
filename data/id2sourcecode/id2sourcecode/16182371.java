    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "_get_MyAdmin", "_get_MyType", "add_filter", "connect_structured_push_supplier", "disconnect_structured_push_consumer", "get_all_filters", "get_filter", "get_qos", "obtain_subscription_types", "offer_change", "push_structured_event", "remove_all_filters", "remove_filter", "set_qos", "validate_event_qos", "validate_qos" };
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
                return _OB_att_get_MyAdmin(in, handler);
            case 1:
                return _OB_att_get_MyType(in, handler);
            case 2:
                return _OB_op_add_filter(in, handler);
            case 3:
                return _OB_op_connect_structured_push_supplier(in, handler);
            case 4:
                return _OB_op_disconnect_structured_push_consumer(in, handler);
            case 5:
                return _OB_op_get_all_filters(in, handler);
            case 6:
                return _OB_op_get_filter(in, handler);
            case 7:
                return _OB_op_get_qos(in, handler);
            case 8:
                return _OB_op_obtain_subscription_types(in, handler);
            case 9:
                return _OB_op_offer_change(in, handler);
            case 10:
                return _OB_op_push_structured_event(in, handler);
            case 11:
                return _OB_op_remove_all_filters(in, handler);
            case 12:
                return _OB_op_remove_filter(in, handler);
            case 13:
                return _OB_op_set_qos(in, handler);
            case 14:
                return _OB_op_validate_event_qos(in, handler);
            case 15:
                return _OB_op_validate_qos(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }
