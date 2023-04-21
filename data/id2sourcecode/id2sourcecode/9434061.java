        @Override
        void execute(Connection conn, Component parent, String context, ProgressMonitor progressBar, ProgressWrapper progressWrapper) throws Exception {
            try {
                conn.setAutoCommit(false);
                DbCommon.SqlAndMode pStmtInfo = getCompositionPrepStmt(conn);
                PreparedStatement pStmt = pStmtInfo.m_pStmt;
                pStmt.executeUpdate();
                if (pStmtInfo.m_isUpdate) {
                    pStmt.clearParameters();
                } else {
                    Integer compositionId = DbCommon.getAutoGenId(parent, context, m_pstmtInsert);
                    pStmt.clearParameters();
                    if (compositionId == null) return;
                    m_compositionId = compositionId.intValue();
                }
                List<PropertiesAndId> propList = null;
                if (m_compEntity == null) {
                    propList = DbTrack.saveTracks(m_parent, conn, m_compositionId);
                } else {
                    DbTrack.saveTrack0(m_parent, conn, m_compositionId, m_compEntity);
                }
                conn.commit();
                if (m_compEntity == null) {
                    DbTrack.updateProperties(propList, m_compositionId);
                    Track0Properties.setLoadedComposition(m_compositionId);
                    s_compostionCache.update(new Integer(m_compositionId), new CompositionParams(m_compositionId, Track0Properties.getCompositionName(), Track0Properties.getSoundFile(), Track0Properties.getFrequency().getVal(), Track0Properties.useMidiPitches()));
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            Track0Properties.markCompositionAsUnedited(true);
                        }
                    });
                } else {
                    m_compEntity.setCompositionId(m_compositionId);
                }
            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                throw ex;
            }
        }
