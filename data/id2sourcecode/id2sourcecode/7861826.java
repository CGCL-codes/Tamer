    @JRubyMethod(name = "write_large_object", required = 6)
    public IRubyObject write_large_object(ThreadContext context, final IRubyObject[] args) throws SQLException, IOException {
        final Ruby runtime = context.getRuntime();
        return (IRubyObject) withConnectionAndRetry(context, new SQLBlock() {

            public Object call(Connection c) throws SQLException {
                String sql = "UPDATE " + rubyApi.convertToRubyString(args[2]) + " SET " + rubyApi.convertToRubyString(args[1]) + " = ? WHERE " + rubyApi.convertToRubyString(args[3]) + "=" + rubyApi.convertToRubyString(args[4]);
                PreparedStatement ps = null;
                try {
                    ps = c.prepareStatement(sql);
                    if (args[0].isTrue()) {
                        ByteList outp = rubyApi.convertToRubyString(args[5]).getByteList();
                        ps.setBinaryStream(1, new ByteArrayInputStream(outp.bytes, outp.begin, outp.realSize), outp.realSize);
                    } else {
                        String ss = rubyApi.convertToRubyString(args[5]).getUnicodeValue();
                        ps.setCharacterStream(1, new StringReader(ss), ss.length());
                    }
                    ps.executeUpdate();
                } finally {
                    close(ps);
                }
                return runtime.getNil();
            }
        });
    }
