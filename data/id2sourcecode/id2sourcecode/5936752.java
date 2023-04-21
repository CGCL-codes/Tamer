    public IStatus execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        final boolean debug = NofdpAnalysisDebug.WATER_STORAGE_SUITABILITY_DEBUG.isEnabled();
        final List<GMLWorkspace> workspaces = new LinkedList<GMLWorkspace>();
        for (final IGeodataSet dataset : m_datasets) {
            try {
                final File shape = ShapeUtils.getShapeFileFromGeodataSet(m_project, dataset);
                workspaces.add(ShapeUtils.getShapeWorkspace(shape));
            } catch (final Exception e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                return ExceptionHelper.getMultiState(this.getClass(), Messages.WSGenerator_1, new IStatus[] { StatusUtilities.createErrorStatus(e.getMessage()) });
            }
            MonitorUtils.step(monitor);
        }
        final WS_SEASON season = m_selection.getSeason();
        final WS_TYPE type = WSGmlUtils.getTypeFromSelection(m_selection.getFeature());
        String[] resultNames = new String[] {};
        final QName wsType = WSGmlUtils.getWSShapeTargetColumnType(season);
        if (WS_TYPE.eGeneral.equals(type)) {
            resultNames = new String[] { "FREQ", "DURATION", "DEPTH", "VSS_GEN" };
        } else if (WS_TYPE.eFloodPlains.equals(type)) {
            resultNames = new String[] { "FREQ", "DURATION", "DEPTH", "VSS_FP" };
        } else throw new IllegalStateException();
        final QName qn1 = SuitabilityGmlUtils.getDatasetDefaultColumn(workspaces.get(0).getGMLSchema().getTargetNamespace(), m_datasets[0]);
        final QName qn2 = SuitabilityGmlUtils.getDatasetDefaultColumn(workspaces.get(1).getGMLSchema().getTargetNamespace(), m_datasets[1]);
        IShapeComparatorWorkspaceDelegate delegate1 = new SuitabilityShapeDelegate(workspaces.get(0), qn1, resultNames[0], m_datasets[0]);
        IShapeComparatorWorkspaceDelegate delegate2 = new SuitabilityShapeDelegate(workspaces.get(1), qn2, resultNames[1], m_datasets[1]);
        SuitabilityShapeResultDelegate result = new SuitabilityShapeResultDelegate(delegate1, delegate2, wsType);
        monitor.subTask(String.format(Messages.WSGenerator_0, workspaces.size() - 1));
        ShapeComparator comparator = new ShapeComparator(delegate1, delegate2, result);
        IStatus status = comparator.execute(monitor);
        if (IStatus.ERROR == status.getSeverity()) return status;
        if (debug) {
            SuitabilityHelper.writeDebug(result);
        }
        GMLWorkspace workspace = result.getWorkspace();
        delegate1.dispose();
        delegate2.dispose();
        if (monitor.isCanceled()) return null;
        MonitorUtils.step(monitor);
        for (int i = 2; i < workspaces.size(); i++) {
            monitor.subTask(String.format(Messages.WSGenerator_20, i, workspaces.size() - 1));
            final QName qni = SuitabilityGmlUtils.getDatasetDefaultColumn(workspaces.get(i).getGMLSchema().getTargetNamespace(), m_datasets[i]);
            delegate1 = new SuitabilityShapeDelegate(workspace, null, null, null);
            delegate2 = new SuitabilityShapeDelegate(workspaces.get(i), qni, resultNames[i], m_datasets[i]);
            result = new SuitabilityShapeResultDelegate(delegate1, delegate2, wsType);
            comparator = new ShapeComparator(delegate1, delegate2, result);
            status = comparator.execute(monitor);
            if (IStatus.ERROR == status.getSeverity()) return status;
            if (debug) {
                SuitabilityHelper.writeDebug(result);
            }
            workspace = result.getWorkspace();
            delegate1.dispose();
            delegate2.dispose();
            if (monitor.isCanceled()) return null;
            MonitorUtils.step(monitor);
        }
        final FeatureList myList = ShapeDissolverTools.getFeatureListFromRoot(workspace.getRootFeature());
        if (myList.size() == 0) throw new CoreException(StatusUtilities.createErrorStatus(Messages.WSGenerator_19));
        setTargetValueColumn(myList, wsType);
        final List<Feature> myFeatures = new ArrayList<Feature>();
        for (final Object object : myList) {
            if (!(object instanceof Feature)) {
                continue;
            }
            final Feature f = (Feature) object;
            myFeatures.add(f);
        }
        try {
            final IFile shapeFile = generateShapeFile(myFeatures.toArray(new Feature[] {}), wsType);
            MonitorUtils.step(monitor);
            final IGeodataCategory category = GeneralConfigGmlUtil.getWaterStorageSuitabilityCategory(m_pool);
            if (category == null) throw new IllegalStateException();
            final WSByPassImportWizard bypass = new WSByPassImportWizard(shapeFile);
            m_menuPart.setAdapterParameter(DelegateShapeExportConfig.class, new DelegateShapeExportConfig(category, shapeFile, bypass));
            final IFile iTemplate = BaseGeoUtils.getStyleTemplateForCategory(category);
            final File fTemplateSld = iTemplate.getLocation().toFile();
            final IFolder tmpFolder = m_project.getFolder(NofdpIDSSConstants.NOFDP_TMP_FOLDER);
            final File fWorkingSld = new File(tmpFolder.getLocation().toFile(), WSGenerator.WATER_STORAGE_FILE_NAME + ".sld");
            FileUtils.copyFile(fTemplateSld, fWorkingSld);
            MonitorUtils.step(monitor);
            final StyleReplacerSuitability styleReplacer = new StyleReplacerSuitability(category, fWorkingSld, wsType.getLocalPart());
            final boolean replace = styleReplacer.replace();
            if (!replace) throw new IllegalStateException();
            MonitorUtils.step(monitor);
            m_menuPart.setAdapterParameter(IDelegateMapExportConfig.class, new WSMapExportConfig(m_pool, category, bypass));
            addMapResult(shapeFile, styleReplacer.getViewName());
            new UIJob("") {

                @Override
                public IStatus runInUIThread(final IProgressMonitor monitor) {
                    SuitabilityHelper.zoomToResultLayer(m_menuPart.getMapView());
                    return Status.OK_STATUS;
                }
            }.schedule(1000);
        } catch (final Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            return ExceptionHelper.getMultiState(this.getClass(), Messages.WSGenerator_22, new IStatus[] { StatusUtilities.createErrorStatus(e.getMessage()) });
        }
        return Status.OK_STATUS;
    }
