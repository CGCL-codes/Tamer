    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "_get_admin_if", "_get_link_if", "_get_lookup_if", "_get_proxy_if", "_get_register_if", "_get_supports_dynamic_properties", "_get_supports_modifiable_properties", "_get_supports_proxy_offers", "_get_type_repos", "describe", "export", "modify", "resolve", "withdraw", "withdraw_using_constraint" };
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
                return _OB_att_get_admin_if(in, handler);
            case 1:
                return _OB_att_get_link_if(in, handler);
            case 2:
                return _OB_att_get_lookup_if(in, handler);
            case 3:
                return _OB_att_get_proxy_if(in, handler);
            case 4:
                return _OB_att_get_register_if(in, handler);
            case 5:
                return _OB_att_get_supports_dynamic_properties(in, handler);
            case 6:
                return _OB_att_get_supports_modifiable_properties(in, handler);
            case 7:
                return _OB_att_get_supports_proxy_offers(in, handler);
            case 8:
                return _OB_att_get_type_repos(in, handler);
            case 9:
                return _OB_op_describe(in, handler);
            case 10:
                return _OB_op_export(in, handler);
            case 11:
                return _OB_op_modify(in, handler);
            case 12:
                return _OB_op_resolve(in, handler);
            case 13:
                return _OB_op_withdraw(in, handler);
            case 14:
                return _OB_op_withdraw_using_constraint(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }
