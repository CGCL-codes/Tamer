    public static void printMd5(HttpServletRequest request) {
        final StringBuilder sb = new StringBuilder();
        sb.append(request.getRemoteAddr()).append(";");
        sb.append(request.getHeader("USER-AGENT"));
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] dg = md.digest(sb.toString().getBytes("utf-8"));
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("\n#######################################");
            sb2.append("string: ").append(sb).append("\n");
            sb2.append("md5: ").append(new String(dg, "utf-8"));
            sb2.append("\n#######################################");
            System.out.println(sb2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
