    public static void writeSubjectiveScores(final Connection connection, final Document challengeDocument, final OutputStream stream) throws IOException, SQLException {
        final Map<Integer, Team> tournamentTeams = Queries.getTournamentTeams(connection);
        final int tournament = Queries.getCurrentTournament(connection);
        final ZipOutputStream zipOut = new ZipOutputStream(stream);
        final Charset charset = Charset.forName("UTF-8");
        final Writer writer = new OutputStreamWriter(zipOut, charset);
        zipOut.putNextEntry(new ZipEntry("challenge.xml"));
        XMLUtils.writeXML(challengeDocument, writer, "UTF-8");
        zipOut.closeEntry();
        zipOut.putNextEntry(new ZipEntry("score.xml"));
        final Document scoreDocument = DownloadSubjectiveData.createSubjectiveScoresDocument(challengeDocument, tournamentTeams.values(), connection, tournament);
        XMLUtils.writeXML(scoreDocument, writer, "UTF-8");
        zipOut.closeEntry();
        zipOut.close();
    }
