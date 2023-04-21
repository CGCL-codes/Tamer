    @Override
    protected void initializeGraphicalViewer() {
        boolean previousIsDirty = false;
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.addDropTargetListener(new FileTransferDropTargetListener(viewer));
        IEditorInput input = getEditorInput();
        Chaine chaine = null;
        if (input instanceof IFileEditorInput) {
            IPath path = ((IFileEditorInput) input).getFile().getLocation();
            try {
                FileInputStream fistream = new FileInputStream(path.toString());
                ObjectInputStream oistream = new ObjectInputStream(fistream);
                Object chaineObj = oistream.readObject();
                if (chaineObj instanceof Chaine) {
                    chaine = (Chaine) chaineObj;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (InvalidClassException e) {
                Application.logger.error("La chaîne a été créée avec une version précédente de PCreator, elle ne peut être ouverte.\n" + "        Une nouvelle chaîne par défaut a été créée. Si vous l'enregistrez, elle écrasera la précédente.");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (chaine == null) {
            chaine = createChaine();
            previousIsDirty = true;
        }
        chaine.addPropertyChangeListener(this);
        viewer.setContents(chaine);
        this.setPartName(chaine.getNom());
        getSite().setSelectionProvider(this);
        chaine.getListeners().firePropertyChange(Element.PROPERTY_MISE_EN_NIVEAUX, null, chaine);
        isDirty = previousIsDirty;
    }
