    @Override
    public void mouseUp(final MouseEvent mouseE) {
        boolean overwrite = true;
        final String indexName = fIndexerView.getIndexNameCombo().getText();
        final String pathToIndex = fIndexerView.getPathToIndexText().getText();
        try {
            final String indexDir = KadosuApplication.getIndexDirectory();
            final String indexPath = Utils.concatePaths(indexDir, indexName);
            final Collection indexDirC = Utils.getLuceneIndexes(indexDir);
            if (indexDirC.contains(indexName)) {
                final int buttonPressed = fIndexerView.openDecisionMsgBox("Index allready exists", "The index you want to create allready exists.\nOverwrite it?");
                switch(buttonPressed) {
                    case SWT.YES:
                        overwrite = true;
                        break;
                    case SWT.NO:
                        overwrite = false;
                        break;
                    default:
                        throw new KDSCancelException("Indexing cancelled");
                }
            }
            final KadosuIndexer job = new KadosuIndexer(pathToIndex, indexPath, overwrite);
            job.setUser(true);
            job.addJobChangeListener(job);
            job.setPriority(Job.LONG);
            job.schedule();
            KadosuPlugin.getProgressService().showInDialog(fIndexerView.getShell(), job);
            fIndexerView.initIndexNameCombo();
        } catch (final IOException e) {
            fIndexerView.openErrorMsgBox("IOException", "Possibly can't create index " + indexName + " in your home dir.\n" + e.getLocalizedMessage());
        } catch (final KDSCancelException e) {
        }
    }
