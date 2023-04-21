    public void loop_executing_okbc_requests(PushableBufferedReader read_stream, PrintWriter write_stream, boolean catch_errors_p, boolean backtrace_p) throws DisconnectException {
        while (true) {
            int reply_tag;
            String line = null;
            while (true) {
                try {
                    do {
                        line = read_stream.readLine();
                    } while (line != null && !(line.equals(request_cookie)));
                    line = read_stream.readLine();
                } catch (IOException e) {
                    System.out.println("Exception in tag read: " + e);
                }
                break;
            }
            if (line == null) {
                throw new DisconnectException();
            }
            reply_tag = Integer.parseInt(line);
            Node find_args = null;
            Node form = null;
            try {
                find_args = decode(read_stream, null, false, LocalConnection.local_connection());
                form = decode(read_stream, null, false, LocalConnection.local_connection());
            } catch (Exception e) {
                System.out.println("Error found whilst processing command.  " + "reply_tag = " + reply_tag + ", find_args = " + find_args + ", form = " + form + ", exception = " + e);
            } catch (Error e) {
                System.out.println("Error found whilst processing command.  " + "reply_tag = " + reply_tag + ", find_args = " + find_args + ", form = " + form + ", exception = " + e);
            }
            if (find_args != null && form != null) {
                messages = _NIL;
                ok_to_cache_p = _T;
                handle_okbc_request(write_stream, form, find_args, reply_tag, catch_errors_p, backtrace_p);
            }
        }
    }
