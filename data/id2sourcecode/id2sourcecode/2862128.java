    public void process(List<Movie> movies, File maybeTarget) throws BusinessException {
        LOG.info("Going to export " + movies.size() + " movies to target '" + maybeTarget.getAbsolutePath() + "'.");
        boolean processFinishedSuccessfully = false;
        File createdDir = null;
        try {
            final boolean coversEnabled = columns.contains(HtmlColumn.COLUMN_COVER) && gotAnyMovieCoverSet(movies);
            final File target;
            if (coversEnabled == true) {
                final File[] targetFileAndTargetDir = copyCoverFiles(movies, maybeTarget);
                LOG.info("Resetting target file to '" + maybeTarget.getAbsolutePath() + "'.");
                target = targetFileAndTargetDir[0];
                createdDir = targetFileAndTargetDir[1];
            } else {
                target = maybeTarget;
            }
            this.targetFilePath = target.getAbsolutePath();
            final String currentDate = CURRENT_DATE_FORMAT.format(new Date());
            BufferedWriter writer = null;
            try {
                LOG.debug("Opening writer for file '" + target.getAbsolutePath() + "'.");
                writer = new BufferedWriter(new FileWriter(target));
                writer.write("<html>\n" + "<head>\n" + "<title>OurMovies - Movies from " + PreferencesDao.getInstance().getUsername() + "</title>\n");
                writer.write(getHeadContent());
                writer.write("</head>\n" + "\n" + "<body>\n");
                LOG.debug("Writing contents of wz_tooltip.js");
                try {
                    writer.write(getContentsOfWzTooltipJs());
                } catch (PtException e) {
                    throw new BusinessException("Could not get contents of wz_tooltip.js file!", e);
                }
                writer.write("<h1>Movies from " + PreferencesDao.getInstance().getUsername() + "</h1>\n" + "<div id='date'>" + currentDate + "</div>\n" + "<br />\n" + "<form id='data_form'>\n" + "\n" + "<table id='tbl_data' cellspacing='0' cellpadding='12'>\n" + "   <colgroup>\n");
                for (HtmlColumn column : this.columns) {
                    writer.write("       <col width='" + column.getWidth() + "' valign='middle' /> <!-- " + column.getLabel() + " -->\n");
                }
                writer.write("   </colgroup>\n" + "   <tr>\n");
                for (HtmlColumn column : this.columns) {
                    writer.write("       <th class='th'>" + column.getLabel() + "</th>\n");
                }
                writer.write("   </tr>\n");
                boolean isEven = true;
                LOG.debug("Wrting table rows for " + movies.size() + " movies.");
                for (final Movie movie : movies) {
                    writer.write(this.getHtmlCodeForMovie(movie, isEven));
                    isEven = !isEven;
                }
                writer.write("</table>" + "\n" + "<div id='outputIdsWrapper'>\n" + "   <textarea id='outputIds' readonly='readonly'></textarea>\n" + "   <div id='btnGenerate'><input type='submit' value='Generate ID Request' onclick='doGenerateIds();return false' /></div>\n" + "</div>\n" + "\n" + "</form>\n" + "\n" + "<p id='footer'>Created with OurMovies v" + BeanFactory.getInstance().getCurrentApplicationVersion().getVersionString() + ": <a id='footer_link' href='http://omov.sourceforge.net' target='_blank'>http://omov.sourceforge.net</a></p>" + "</body>\n" + "</html>\n");
            } catch (IOException e) {
                throw new BusinessException("Could not generate HTML report!", e);
            } finally {
                PtCloseUtil.close(writer);
            }
            LOG.info("Exporting HTML finished.");
            processFinishedSuccessfully = true;
        } finally {
            if (processFinishedSuccessfully == false && createdDir != null) {
                LOG.info("Going to delete created directory '" + createdDir.getAbsolutePath() + "' because exporting html failed.");
                try {
                    PtFileUtil.deleteDirectoryRecursive(createdDir);
                } catch (PtException e) {
                    LOG.error("Could not delete created directory '" + createdDir.getAbsolutePath() + "'!", e);
                }
            }
        }
    }
