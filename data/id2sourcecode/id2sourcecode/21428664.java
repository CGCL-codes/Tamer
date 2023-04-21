    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "_get_incarnation", "add_type", "describe_type", "fully_describe_type", "list_types", "mask_type", "remove_type", "unmask_type" };
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
                return _OB_att_get_incarnation(in, handler);
            case 1:
                return _OB_op_add_type(in, handler);
            case 2:
                return _OB_op_describe_type(in, handler);
            case 3:
                return _OB_op_fully_describe_type(in, handler);
            case 4:
                return _OB_op_list_types(in, handler);
            case 5:
                return _OB_op_mask_type(in, handler);
            case 6:
                return _OB_op_remove_type(in, handler);
            case 7:
                return _OB_op_unmask_type(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }
