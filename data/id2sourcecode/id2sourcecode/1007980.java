    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "define_properties", "define_property", "delete_all_properties", "delete_properties", "delete_property", "get_all_properties", "get_all_property_names", "get_number_of_properties", "get_properties", "get_property_value", "is_property_defined" };
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
                return _OB_op_define_properties(in, handler);
            case 1:
                return _OB_op_define_property(in, handler);
            case 2:
                return _OB_op_delete_all_properties(in, handler);
            case 3:
                return _OB_op_delete_properties(in, handler);
            case 4:
                return _OB_op_delete_property(in, handler);
            case 5:
                return _OB_op_get_all_properties(in, handler);
            case 6:
                return _OB_op_get_all_property_names(in, handler);
            case 7:
                return _OB_op_get_number_of_properties(in, handler);
            case 8:
                return _OB_op_get_properties(in, handler);
            case 9:
                return _OB_op_get_property_value(in, handler);
            case 10:
                return _OB_op_is_property_defined(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }
