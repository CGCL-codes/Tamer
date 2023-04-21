    public IStatus execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        final boolean debug = NofdpAnalysisDebug.VEGETATION_SUITABILITY_DEBUG.isEnabled();
        final List<GMLWorkspace> workspaces = new LinkedList<GMLWorkspace>();
        for (final IGeodataSet dataset : m_datasets) {
            final File shape = ShapeUtils.getShapeFileFromGeodataSet(m_project, dataset);
            try {
                workspaces.add(ShapeUtils.getShapeWorkspace(dataset, shape));
            } catch (final Exception e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                return ExceptionHelper.getMultiState(this.getClass(), Messages.VSGenerator_2, new IStatus[] { StatusUtilities.createErrorStatus(e.getMessage()) });
            }
            MonitorUtils.step(monitor);
        }
        final QName vsType = VSGmlUtils.getVSShapeTargetColumnType(m_vegetationSuitability);
        String[] resultNames = new String[] {};
        final VS_TYPE type = VsUtils.getSelectedMenuFromFeature(m_vegetationSuitability);
        if (VS_TYPE.eSmall.equals(type)) {
            resultNames = new String[] { "NUTRIENT", "MOISTURE", "VEGETATION" };
        } else if (VS_TYPE.eSmallSalinity.equals(type)) {
            resultNames = new String[] { "NUTRIENT", "MOISTURE", "VEGETATION", "SALINITY" };
        } else if (VS_TYPE.eLarge.equals(type)) {
            resultNames = new String[] { "FLOODPLAIN", "DURATION" };
        } else throw new IllegalStateException();
        final QName qn1 = SuitabilityGmlUtils.getDatasetDefaultColumn(workspaces.get(0).getGMLSchema().getTargetNamespace(), m_datasets[0]);
        final QName qn2 = SuitabilityGmlUtils.getDatasetDefaultColumn(workspaces.get(1).getGMLSchema().getTargetNamespace(), m_datasets[1]);
        SuitabilityShapeDelegate delegate1 = new SuitabilityShapeDelegate(workspaces.get(0), qn1, resultNames[0], m_datasets[0]);
        SuitabilityShapeDelegate delegate2 = new SuitabilityShapeDelegate(workspaces.get(1), qn2, resultNames[1], m_datasets[1]);
        SuitabilityShapeResultDelegate result = new SuitabilityShapeResultDelegate(delegate1, delegate2, vsType);
        monitor.subTask(String.format(Messages.VSGenerator_1, workspaces.size() - 1));
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
        for (int i = 2; i < workspaces.size(); i++) {
            monitor.subTask(String.format(Messages.VSGenerator_0, i, workspaces.size() - 1));
            final QName qni = SuitabilityGmlUtils.getDatasetDefaultColumn(workspaces.get(i).getGMLSchema().getTargetNamespace(), m_datasets[i]);
            delegate1 = new SuitabilityShapeDelegate(workspace, null, null, null);
            delegate2 = new SuitabilityShapeDelegate(workspaces.get(i), qni, resultNames[i], m_datasets[i]);
            result = new SuitabilityShapeResultDelegate(delegate1, delegate2, vsType);
            comparator = new ShapeComparator(delegate1, delegate2, result);
            status = comparator.execute(monitor);
            if (IStatus.ERROR == status.getSeverity()) return status;
            if (debug) {
                SuitabilityHelper.writeDebug(result);
            }
            workspace = result.getWorkspace();
            delegate1.dispose();
            delegate2.dispose();
            if (monitor.isCanceled()) return Status.CANCEL_STATUS;
            MonitorUtils.step(monitor);
        }
        final FeatureList myList = ShapeDissolverTools.getFeatureListFromRoot(workspace.getRootFeature());
        if (myList.size() == 0) throw new CoreException(StatusUtilities.createErrorStatus(Messages.VSGenerator_29));
        setTargetValueColumn(myList, vsType);
        final List<Feature> myFeatures = new ArrayList<Feature>();
        for (final Object object : myList) {
            if (!(object instanceof Feature)) {
                continue;
            }
            final Feature f = (Feature) object;
            myFeatures.add(f);
        }
        try {
            final IFile shapeFile = generateShapeFile(myFeatures.toArray(new Feature[] {}), vsType);
            MonitorUtils.step(monitor);
            final IGeodataCategory category = GeneralConfigGmlUtil.getVegetationSuitabilityCategory(m_pool, m_vegetationSuitability.getParentRelation().getQName());
            if (category == null) throw new IllegalStateException();
            final VSByPassImportWizard bypass = new VSByPassImportWizard(shapeFile);
            m_menuPart.setAdapterParameter(DelegateShapeExportConfig.class, new DelegateShapeExportConfig(category, shapeFile, bypass));
            final IFile iTemplate = BaseGeoUtils.getStyleTemplateForCategory(category);
            final File fTemplateSld = iTemplate.getLocation().toFile();
            final IFolder tmpFolder = m_project.getFolder(NofdpIDSSConstants.NOFDP_TMP_FOLDER);
            final File fWorkingSld = new File(tmpFolder.getLocation().toFile(), VSGenerator.VEGETATION_SUITABILITY_FILE_NAME + ".sld");
            FileUtils.copyFile(fTemplateSld, fWorkingSld);
            MonitorUtils.step(monitor);
            final StyleReplacerSuitability styleReplacer;
            if (vsType.equals(GmlConstants.QN_GEODATA_VEGETATION_SUITABILITY_SMALL)) {
                styleReplacer = new StyleReplacerSuitability(category, fWorkingSld, VSGenerator.VEG_SMALL);
            } else if (vsType.equals(GmlConstants.QN_GEODATA_VEGETATION_SUITABILITY_LARGE)) {
                styleReplacer = new StyleReplacerSuitability(category, fWorkingSld, VSGenerator.VEG_LARGE);
            } else throw new NotImplementedException();
            final boolean replace = styleReplacer.replace();
            if (!replace) throw new IllegalStateException();
            MonitorUtils.step(monitor);
            m_menuPart.setAdapterParameter(IDelegateMapExportConfig.class, new VSMapExportConfig(m_pool, category, bypass));
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
            return ExceptionHelper.getMultiState(this.getClass(), Messages.VSGenerator_31, new IStatus[] { StatusUtilities.createErrorStatus(e.getMessage()) });
        }
        return Status.OK_STATUS;
    }
