        public static void http(HttpServletRequest req, OutputStream ops, MODE mode) {
            InputStream ins;
            int b;
            try {
                if (MODE.ALL == mode || MODE.HEADER_ONLY == mode) {
                    StringBuilder sb = new StringBuilder();
                    Enumeration<?> ens = req.getHeaderNames();
                    while (ens.hasMoreElements()) {
                        String name = ens.nextElement().toString();
                        sb.append(name).append(": ").append(req.getHeader(name)).append("\r\n");
                    }
                    sb.append("\r\n");
                    ins = Lang.ins(sb);
                    while (-1 != (b = ins.read())) ops.write(b);
                }
                if (MODE.ALL == mode || MODE.BODY_ONLY == mode) {
                    ins = req.getInputStream();
                    while (-1 != (b = ins.read())) ops.write(b);
                    ins.close();
                }
                ops.flush();
                ops.close();
            } catch (IOException e) {
                throw Lang.wrapThrow(e);
            }
        }
