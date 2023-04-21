    @Override
    public PyObject __call__(PyObject[] args, String[] keywords) {
        PyObject False = Py.newBoolean(false);
        FunctionSupport fs = new FunctionSupport("plotx", new String[] { "x", "y", "z", "xtitle", "xrange", "ytitle", "yrange", "ztitle", "zrange", "xlog", "ylog", "zlog", "title", "renderType", "color", "symsize", "linewidth", "symbol", "isotropic" }, new PyObject[] { Py.None, Py.None, Py.None, Py.None, Py.None, Py.None, Py.None, Py.None, False, False, False, Py.None, Py.None, Py.None, Py.None, Py.None, Py.None, Py.None });
        fs.args(args, keywords);
        int nparm = args.length - keywords.length;
        if (nparm == 0) {
            System.err.println("args.length=0");
            return Py.None;
        }
        int iplot = 0;
        int nargs = nparm;
        PyObject po0 = args[0];
        if (po0 instanceof PyInteger) {
            iplot = ((PyInteger) po0).getValue();
            PyObject[] newArgs = new PyObject[args.length - 1];
            for (int i = 0; i < args.length - 1; i++) {
                newArgs[i] = args[i + 1];
            }
            args = newArgs;
            nargs = nargs - 1;
            nparm = args.length - keywords.length;
        }
        QDataSet[] qargs = new QDataSet[nargs];
        Application dom = ScriptContext.getDocumentModel();
        if (nargs == 1 && po0 instanceof PyString) {
            try {
                ScriptContext.plot(((PyString) po0).toString());
            } catch (InterruptedException ex) {
                Logger.getLogger(PlotCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            for (int i = 0; i < nargs; i++) {
                QDataSet ds = coerceIt(args[i]);
                qargs[i] = ds;
            }
            try {
                if (nargs == 1) {
                    ScriptContext.plot(iplot, qargs[0]);
                } else if (nargs == 2) {
                    ScriptContext.plot(iplot, qargs[0], qargs[1]);
                } else if (nargs == 3) {
                    ScriptContext.plot(iplot, qargs[0], qargs[1], qargs[2]);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
        dom.getController().registerPendingChange(this, this);
        dom.getController().performingChange(this, this);
        try {
            int chNum = iplot;
            while (dom.getDataSourceFilters().length <= chNum) {
                Plot p = CanvasUtil.getMostBottomPlot(dom.getController().getCanvas());
                dom.getController().setPlot(p);
                dom.getController().addPlotElement(null, null);
            }
            DataSourceFilter dsf = dom.getDataSourceFilters(chNum);
            List<PlotElement> elements = dom.getController().getPlotElementsFor(dsf);
            Plot plot = dom.getController().getPlotFor(elements.get(0));
            plot.setIsotropic(false);
            for (int i = nparm; i < args.length; i++) {
                String kw = keywords[i - nparm];
                PyObject val = args[i];
                String sval = (String) val.__str__().__tojava__(String.class);
                if (kw.equals("ytitle")) {
                    plot.getYaxis().setLabel(sval);
                } else if (kw.equals("yrange")) {
                    DatumRange dr = plot.getYaxis().getRange();
                    Units u = dr.getUnits();
                    PyList plval = (PyList) val;
                    plot.getYaxis().setRange(DatumRange.newDatumRange(((Number) plval.get(0)).doubleValue(), ((Number) plval.get(1)).doubleValue(), u));
                } else if (kw.equals("ylog")) {
                    plot.getYaxis().setLog("1".equals(sval));
                } else if (kw.equals("xtitle")) {
                    plot.getXaxis().setLabel(sval);
                } else if (kw.equals("xrange")) {
                    DatumRange dr = plot.getXaxis().getRange();
                    Units u = dr.getUnits();
                    PyList plval = (PyList) val;
                    plot.getXaxis().setRange(DatumRange.newDatumRange(((Number) plval.get(0)).doubleValue(), ((Number) plval.get(1)).doubleValue(), u));
                } else if (kw.equals("xlog")) {
                    plot.getXaxis().setLog("1".equals(sval));
                } else if (kw.equals("ztitle")) {
                    plot.getZaxis().setLabel(sval);
                } else if (kw.equals("zrange")) {
                    DatumRange dr = plot.getZaxis().getRange();
                    Units u = dr.getUnits();
                    PyList plval = (PyList) val;
                    plot.getZaxis().setRange(DatumRange.newDatumRange(((Number) plval.get(0)).doubleValue(), ((Number) plval.get(1)).doubleValue(), u));
                } else if (kw.equals("zlog")) {
                    plot.getZaxis().setLog("1".equals(sval));
                } else if (kw.equals("color")) {
                    if (sval != null) {
                        Color c;
                        try {
                            c = Color.decode(sval);
                        } catch (NumberFormatException ex) {
                            c = (Color) getEnumElement(Color.class, sval);
                        }
                        if (c != null) {
                            elements.get(0).getStyle().setColor(c);
                        } else {
                            throw new IllegalArgumentException("unable to identify color: " + sval);
                        }
                    }
                } else if (kw.equals("title")) {
                    plot.setTitle(sval);
                } else if (kw.equals("symsize")) {
                    elements.get(0).getStyle().setSymbolSize(Double.valueOf(sval));
                } else if (kw.equals("linewidth")) {
                    elements.get(0).getStyle().setLineWidth(Double.valueOf(sval));
                } else if (kw.equals("symbol")) {
                    PlotSymbol p = (PlotSymbol) getEnumElement(DefaultPlotSymbol.class, sval);
                    if (p != null) {
                        elements.get(0).getStyle().setPlotSymbol(p);
                    } else {
                        throw new IllegalArgumentException("unable to identify symbol: " + sval);
                    }
                } else if (kw.equals("renderType")) {
                    RenderType rt = RenderType.valueOf(sval);
                    elements.get(0).setRenderType(rt);
                } else if (kw.equals("isotropic")) {
                    plot.setIsotropic(true);
                }
            }
        } finally {
            dom.getController().changePerformed(this, this);
        }
        return Py.None;
    }
