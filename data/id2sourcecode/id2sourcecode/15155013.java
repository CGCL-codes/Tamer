    public boolean guardarRespuestasDePreguntaDeExamen(int codExam, int codPreg, int[] respuestas) {
        Transaction tx = null;
        Session session = HibernateUtil.currentSession();
        SQLQuery query = null;
        boolean guardado = false;
        try {
            tx = session.beginTransaction();
            session.clear();
            Examen examen = (Examen) session.get(Examen.class, codExam);
            Pregunta pregunta = (Pregunta) session.get(Pregunta.class, codPreg);
            for (int i = 0; i < respuestas.length; i++) {
                Respuesta resp = (Respuesta) session.get(Respuesta.class, respuestas[i]);
                query = session.createSQLQuery("INSERT INTO respuestas_pregunta_examen (codExam,codPreg,codResp) VALUES (?,?,?)");
                query.setInteger(0, codExam);
                query.setInteger(1, codPreg);
                query.setInteger(2, resp.getCodResp());
                if (query.executeUpdate() <= 0) {
                    break;
                }
            }
            tx.commit();
            guardado = true;
        } catch (Exception e) {
            try {
                tx.rollback();
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            HibernateUtil.closeSession();
        }
        return guardado;
    }
