    public final synchronized CDRInputStream invoke() throws org.omg.CORBA.portable.ApplicationException, org.omg.CORBA.portable.RemarshalException {
        java.io.OutputStream http_out = null;
        java.io.InputStream http_in = null;
        try {
            CDROutputStream cdr_out = outputCDR;
            int[] align_offts = cdr_out.get_align_offsets();
            CDR.write4b(requestHdr, HREQ_ALOFF_2, align_offts[0]);
            CDR.write4b(requestHdr, HREQ_ALOFF_4, align_offts[1]);
            CDR.write4b(requestHdr, HREQ_ALOFF_8, align_offts[2]);
            URLConnection conn = url_.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-orbgate-cdr");
            http_out = conn.getOutputStream();
            http_out.write(requestHdr, 0, HDR_SIZE_REQUEST);
            cdr_out.write_to(http_out);
            http_out.close();
            http_out = null;
            http_in = conn.getInputStream();
            int reply_size = conn.getContentLength();
            MsgBlk reply_blk = new MsgBlk(reply_size);
            byte[] reply_buf = reply_blk.data;
            for (int nrd = 0; nrd < reply_size; ) {
                int n = http_in.read(reply_buf, nrd, reply_size - nrd);
                if (n <= 0) throw new java.io.EOFException();
                nrd += n;
            }
            reply_blk.used = reply_size;
            http_in.close();
            http_in = null;
            int byte_order = reply_buf[HREP_BYTE_ORDER];
            int call_status = reply_buf[HREP_CALL_STATUS];
            int padding = reply_buf[HREP_PADDING];
            orb_.set_preferred_byte_order(reply_buf[HREP_REQ_BYTE_ORDER]);
            CDRInputStream cdr_in = (CDRInputStream) cdr_out.create_input_stream();
            cdr_in.open(reply_blk, HDR_SIZE_REPLY, reply_size - HDR_SIZE_REPLY);
            cdr_in.set_byte_order(byte_order);
            cdr_in.set_stream_pos(padding);
            switch(call_status) {
                case STATUS_SUCCESS:
                    return cdr_in;
                case STATUS_SYSTEM_EXCEPTION:
                    {
                        String id = cdr_in.read_string();
                        int minor = cdr_in.read_long();
                        org.omg.CORBA.CompletionStatus cmpl = org.omg.CORBA.CompletionStatus.from_int(cdr_in.read_long());
                        throw orb_.create_system_exception(id, minor, cmpl);
                    }
                case STATUS_USER_EXCEPTION:
                    {
                        String id = cdr_in.read_string();
                        cdr_in.open(reply_blk, HDR_SIZE_REPLY, reply_size - HDR_SIZE_REPLY);
                        cdr_in.set_byte_order(byte_order);
                        cdr_in.set_stream_pos(padding);
                        throw new org.omg.CORBA.portable.ApplicationException(id, cdr_in);
                    }
                case STATUS_RESEND_REQUEST:
                    throw new org.omg.CORBA.portable.RemarshalException();
            }
            throw new org.omg.CORBA.UNKNOWN("Unknown status code: " + call_status, 0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        } catch (UnknownHostException ex) {
            throw new org.omg.CORBA.COMM_FAILURE(ex.toString(), 0, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        } catch (NoRouteToHostException ex) {
            throw new org.omg.CORBA.COMM_FAILURE(ex.toString(), 0, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        } catch (ConnectException ex) {
            throw new org.omg.CORBA.COMM_FAILURE(ex.toString(), 0, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        } catch (IOException ex) {
            throw new org.omg.CORBA.COMM_FAILURE(ex.toString(), 0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        } finally {
            if (http_out != null) {
                try {
                    http_out.close();
                } catch (IOException ex) {
                }
            }
            if (http_in != null) {
                try {
                    http_in.close();
                } catch (IOException ex) {
                }
            }
        }
    }
