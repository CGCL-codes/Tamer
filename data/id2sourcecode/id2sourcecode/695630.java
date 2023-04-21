    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int op = Integer.parseInt(request.getParameter("op"));
            String id = "";
            switch(op) {
                case (1):
                    String user, passwd;
                    user = request.getParameter("user");
                    passwd = request.getParameter("passwd");
                    String session = "", userid = "";
                    session = operation(user, passwd);
                    if (session.equals("Usuario No Registrado")) {
                        out.println("<h2>" + session + "</h2>");
                    } else {
                        if (session.equals("Password incorrecto")) {
                            out.println("<h2>" + session + "</h2>");
                        } else {
                            out.println("<h2>Has sido logueado correctamente </br> " + session + "</h2>" + "<br ><a href='datos_personales.jsp'>Actualizar Datos Personales</a>");
                            userid = hello(user);
                            HttpSession sesion = request.getSession(true);
                            sesion.setAttribute("id", userid);
                            sesion.setAttribute("nombrec", session);
                        }
                    }
                    break;
                case (2):
                    String nombres, cedula, apellidos, userr, passwdr, emailr;
                    nombres = request.getParameter("nombres");
                    apellidos = request.getParameter("apellidos");
                    cedula = request.getParameter("cedula");
                    userr = request.getParameter("userr");
                    passwdr = request.getParameter("passwdr");
                    emailr = request.getParameter("emailr");
                    out.println(registro(cedula, nombres, apellidos, userr, passwdr, emailr));
                    HttpSession sesion = request.getSession(true);
                    sesion.setAttribute("id", cedula);
                    sesion.setAttribute("nombrec", nombres + " " + apellidos);
                    out.println("<h2>Has sido logueado correctamente </br>" + nombres + " " + apellidos + "</h2>" + "<br ><a href='datos_personales.jsp'>Actualizar Datos Personales</a>");
                    break;
                case (3):
                    String busqueda = request.getParameter("busqueda");
                    System.out.print(busqueda);
                    try {
                        URL url = new URL("http://localhost//WSPHPDepartamento//cliente.php?busqueda=" + busqueda);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.getContent();
                        InputStream in = con.getInputStream();
                        StringBuffer data = new StringBuffer();
                        int i;
                        while ((i = in.read()) != -1) {
                            data.append((char) i);
                        }
                        out.println(data);
                    } catch (Exception e) {
                        System.out.println("Fallo al recibir información del script PHP");
                    }
                    break;
                case (4):
                    String usu = "", apel = "", apel2 = "", tid = "", dep1 = "";
                    String mun1 = "", dep2 = "", mun2 = "", fechan = "", dir = "", dep3 = "", mun3 = "", cel = "", tel1 = "", tel2 = "";
                    sesion = request.getSession();
                    id = (String) sesion.getAttribute("id");
                    usu = request.getParameter("usu");
                    apel = request.getParameter("apel");
                    apel2 = request.getParameter("apel2");
                    tid = request.getParameter("tid");
                    dep1 = request.getParameter("dep1");
                    mun1 = request.getParameter("mun1");
                    dep2 = request.getParameter("dep2");
                    mun2 = request.getParameter("mun2");
                    fechan = request.getParameter("fechan");
                    dir = request.getParameter("dir");
                    dep3 = request.getParameter("dep3");
                    mun3 = request.getParameter("mun3");
                    cel = request.getParameter("cel");
                    tel1 = request.getParameter("tel1");
                    tel2 = request.getParameter("tel2");
                    out.println(datosPersonales(id, usu, apel, apel2, tid, dep1, mun1, dep2, mun2, fechan, dir, dep3, mun3, cel, tel1, tel2));
                    break;
                case (5):
                    String programa, facultad, year, nivel, titulo, fecha;
                    sesion = request.getSession();
                    id = (String) sesion.getAttribute("id");
                    programa = request.getParameter("programa");
                    facultad = request.getParameter("facultad");
                    year = request.getParameter("year");
                    nivel = request.getParameter("nivel");
                    titulo = request.getParameter("titulo");
                    fecha = request.getParameter("fecha");
                    out.println(historiaAcademica(id, programa, facultad, year, nivel, titulo, fecha));
                    break;
                case (6):
                    String nom_empr = "", cargo = "", experiencia = "", emp_a_cargo = "", emp_uni = "", opinion = "";
                    String dir_lab = "", dep = "", mun = "", tel_em = "", emaillab = "", emailper = "";
                    sesion = request.getSession();
                    id = (String) sesion.getAttribute("id");
                    nom_empr = request.getParameter("nom_empr");
                    cargo = request.getParameter("cargo");
                    experiencia = request.getParameter("experiencia");
                    emp_a_cargo = request.getParameter("emp_a_cargo");
                    emp_uni = request.getParameter("emp_uni");
                    opinion = request.getParameter("opinion");
                    dir_lab = request.getParameter("dir_lab");
                    dep = request.getParameter("dep");
                    mun = request.getParameter("mun");
                    tel_em = request.getParameter("tel_em");
                    emaillab = request.getParameter("emaillab");
                    emailper = request.getParameter("emailper");
                    out.println(informacionLaboral(id, nom_empr, cargo, experiencia, emp_a_cargo, emp_uni, opinion, dir_lab, dep, mun, tel_em, emaillab, emailper));
                    break;
                default:
                    System.out.println("Opcion No valida");
            }
        } finally {
            out.close();
        }
    }
