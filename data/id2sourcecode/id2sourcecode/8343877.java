    public void handle_connection(ServerSocket server_socket, BufferedReader r_s, PrintWriter write_stream, boolean catch_errors_p, boolean backtrace_p) {
        PushableBufferedReader read_stream = new PushableBufferedReader(r_s);
        Node connect_form = null;
        Exception error = null;
        try {
            connect_form = decode(read_stream, null, false, LocalConnection.local_connection());
        } catch (Exception e) {
            error = e;
        }
        if (connect_form == null) {
            System.out.println("Error found whilst reading connection parameters.  " + error);
            handle_okbc_eval_error(write_stream, error, -1);
        } else {
            Node kb_library_string = Cons.getf(connect_form, _kb_library);
            if (kb_library_string instanceof OKBCString) {
                kb_library = ((OKBCString) kb_library_string).string;
            }
            Node tv_sym = Cons.getf(connect_form, _transport_version);
            if (!(tv_sym instanceof AbstractSymbol) || (((AbstractSymbol) tv_sym).symbolName.compareTo(max_supported_transport_version) > 0) || (((AbstractSymbol) tv_sym).symbolName.compareTo(min_supported_transport_version) < 0)) {
                throw new RuntimeException("Transport version " + tv_sym + " not supported.  Supported = " + min_supported_transport_version + "..." + max_supported_transport_version);
            }
            Node query_sym = Cons.getf(connect_form, _query_format);
            Symbol supported_query = Symbol.keyword(query_format);
            if (query_sym != supported_query) {
                throw new RuntimeException("Query format " + query_sym + " not supported.  Supported = " + supported_query);
            }
            Node reply_sym = Cons.getf(connect_form, _reply_format);
            Symbol supported_reply = Symbol.keyword(reply_format);
            if (reply_sym != supported_reply) {
                throw new RuntimeException("Reply format " + reply_sym + " not supported.  Supported = " + supported_reply);
            }
            Node ok_form = Cons.list(_ok);
            ok_form.encode(write_stream, false);
            _NIL.encode(write_stream, false);
            _NIL.encode(write_stream, false);
            _ok.encode(write_stream, false);
            _NIL.encode(write_stream, false);
            OKBCString ok_string = new OKBCString("Persistent Network OKBC connection established.");
            ok_string.encode(write_stream, false);
            newline(write_stream);
            write_stream.flush();
            try {
                loop_executing_okbc_requests(read_stream, write_stream, catch_errors_p, backtrace_p);
            } catch (DisconnectException d) {
                try {
                    server_socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
