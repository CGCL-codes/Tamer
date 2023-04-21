    public void up(IStructuredSelection selection) {
        Set libraries = getSelectedLibraries(selection);
        for (int i = 0; i < fLibraries.length - 1; i++) {
            if (libraries.contains(fLibraries[i + 1])) {
                LibraryStandin temp = fLibraries[i];
                fLibraries[i] = fLibraries[i + 1];
                fLibraries[i + 1] = temp;
            }
        }
        fViewer.refresh();
        fViewer.setSelection(selection);
    }
