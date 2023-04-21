        @Override
        public void initBindings() {
            addNoneFuncs("parse_and_bind", "insert_text", "read_init_file", "read_history_file", "write_history_file", "clear_history", "set_history_length", "remove_history_item", "replace_history_item", "redisplay", "set_startup_hook", "set_pre_input_hook", "set_completer", "set_completer_delims", "set_completion_display_matches_hook", "add_history");
            addNumFuncs("get_history_length", "get_current_history_length", "get_begidx", "get_endidx");
            addStrFuncs("get_line_buffer", "get_history_item");
            addUnknownFuncs("get_completion_type");
            addFunction("get_completer", liburl(), newFunc());
            addFunction("get_completer_delims", liburl(), newList(BaseStr));
        }
