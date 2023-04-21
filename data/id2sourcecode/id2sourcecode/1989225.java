    private void cargar(Connection conn) throws SQLException, ClassNotFoundException, Exception {
        PreparedStatement ms = null;
        registroGrabado = false;
        Date fechaSystem = new Date();
        DateFormat aaaammdd = new SimpleDateFormat("yyyyMMdd");
        int fzafsis = Integer.parseInt(aaaammdd.format(fechaSystem));
        DateFormat hhmmss = new SimpleDateFormat("HHmmss");
        DateFormat sss = new SimpleDateFormat("S");
        String ss = sss.format(fechaSystem);
        if (ss.length() > 2) {
            ss = ss.substring(0, 2);
        }
        int fzahsis = Integer.parseInt(hhmmss.format(fechaSystem) + ss);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaSystem);
        fechaTest = dateF.parse(fechaOficio);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaTest);
        DateFormat date1 = new SimpleDateFormat("yyyyMMdd");
        int fzaanoe = cal.get(Calendar.YEAR);
        anoOficio = String.valueOf(fzaanoe);
        int dataofici = Integer.parseInt(date1.format(fechaTest));
        int numof = ToolsBD.RecogerNumeroOficio(conn, Integer.parseInt(anoOficio), oficinaOficio, errores);
        numeroOficio = String.valueOf(numof);
        ms = conn.prepareStatement(SENTENCIA);
        ms.setInt(1, Integer.parseInt(anoOficio));
        ms.setInt(2, Integer.parseInt(oficinaOficio));
        ms.setInt(3, numof);
        ms.setInt(4, dataofici);
        ms.setString(5, descripcion);
        if (anoSalida != null) {
            ms.setInt(6, Integer.parseInt(anoSalida));
        } else {
            ms.setInt(6, 0);
        }
        if (oficinaSalida != null) {
            ms.setInt(7, Integer.parseInt(oficinaSalida));
        } else {
            ms.setInt(7, 0);
        }
        if (numeroSalida != null) {
            ms.setInt(8, Integer.parseInt(numeroSalida));
        } else {
            ms.setInt(8, 0);
        }
        ms.setString(9, nulo);
        ms.setString(10, usuarioNulo);
        ms.setString(11, motivosNulo);
        ms.setInt(12, 0);
        if (fechaEntrada != null && !fechaEntrada.equals("")) {
            fechaTest = dateF.parse(fechaEntrada);
            cal.setTime(fechaTest);
            int fzafent = Integer.parseInt(date1.format(fechaTest));
            ms.setInt(13, fzafent);
        } else {
            ms.setInt(13, 0);
        }
        ms.setString(14, descartadoEntrada);
        ms.setString(15, usuarioEntrada);
        ms.setString(16, motivosDescarteEntrada);
        if (anoEntrada != null) {
            ms.setInt(17, Integer.parseInt(anoEntrada));
        } else {
            ms.setInt(17, 0);
        }
        if (oficinaEntrada != null) {
            ms.setInt(18, Integer.parseInt(oficinaEntrada));
        } else {
            ms.setInt(18, 0);
        }
        if (numeroEntrada != null) {
            ms.setInt(19, Integer.parseInt(numeroEntrada));
        } else {
            ms.setInt(19, 0);
        }
        registroGrabado = ms.execute();
        registroGrabado = true;
        ms.close();
    }
