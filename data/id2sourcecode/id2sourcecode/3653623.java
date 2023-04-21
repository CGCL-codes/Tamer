
    /** Add a legend (displayed at the upper right) for the specified
     *  data set with the specified string.  Short strings generally
     *  fit better than long strings.
     *  @param dataset The dataset index.
     *  @param legend The label for the dataset.
     */
    public void addLegend(int dataset, String legend) {
        _checkDatasetIndex(dataset);
        super.addLegend(dataset, legend);
    }

    /** In the specified data set, add the specified x, y point to the
     *  plot.  Data set indices begin with zero.  If the data set
     *  does not exist, create it.  The fourth argument indicates
     *  whether the point should be connected by a line to the previous
     *  point.  The new point will be made visible if the plot is visible
     *  on the screen.  Otherwise, it will be drawn the next time the plot
     *  is drawn on the screen.
     *  @param dataset The data set index.
     *  @param x The X position of the new point.
     *  @param y The Y position of the new point.
     *  @param connected If true, a line is drawn to connect to the previous
     *   point.
     */
    public synchronized void addPoint(int dataset, double x, double y, boolean connected) {
        if (_xlog) {
            if (x <= 0.0) {
                System.err.println("Can't plot non-positive X values " + "when the logarithmic X axis value is specified: " + x);
                return;
            }
            x = Math.log(x) * _LOG10SCALE;
        }
        if (_ylog) {
            if (y <= 0.0) {
                System.err.println("Can't plot non-positive Y values " + "when the logarithmic Y axis value is specified: " + y);
                return;
            }
            y = Math.log(y) * _LOG10SCALE;
        }
        _addPoint(dataset, x, y, 0, 0, connected, false);
    }

    /** In the specified data set, add the specified x, y point to the
     *  plot with error bars.  Data set indices begin with zero.  If
     *  the dataset does not exist, create it.  yLowEB and
     *  yHighEB are the lower and upper error bars.  The sixth argument
     *  indicates whether the point should be connected by a line to
     *  the previous point.
     *  The new point will be made visible if the plot is visible
     *  on the screen.  Otherwise, it will be drawn the next time the plot
     *  is drawn on the screen.
     *  This method is based on a suggestion by
     *  Michael Altmann <michael@email.labmed.umn.edu>.
     *
     *  @param dataset The data set index.
     *  @param x The X position of the new point.
     *  @param y The Y position of the new point.
     *  @param yLowEB The low point of the error bar.
     *  @param yHighEB The high point of the error bar.
     *  @param connected If true, a line is drawn to connect to the previous
     *   point.
     */
    public synchronized void addPointWithErrorBars(int dataset, double x, double y, double yLowEB, double yHighEB, boolean connected) {
        if (_xlog) {
            if (x <= 0.0) {
                System.err.println("Can't plot non-positive X values " + "when the logarithmic X axis value is specified: " + x);
                return;
            }
            x = Math.log(x) * _LOG10SCALE;
        }
        if (_ylog) {
            if (y <= 0.0 || yLowEB <= 0.0 || yHighEB <= 0.0) {
                System.err.println("Can't plot non-positive Y values " + "when the logarithmic Y axis value is specified: " + y);
                return;
            }
            y = Math.log(y) * _LOG10SCALE;
            yLowEB = Math.log(yLowEB) * _LOG10SCALE;
            yHighEB = Math.log(yHighEB) * _LOG10SCALE;
        }
        _addPoint(dataset, x, y, yLowEB, yHighEB, connected, true);
    }

    /** Clear the plot of all data points.  If the argument is true, then
     *  reset all parameters to their initial conditions, including
     *  the persistence, plotting format, and axes formats.
     *  For the change to take effect, you must call repaint().
     *  @param format If true, clear the format controls as well.
     */
    public synchronized void clear(boolean format) {
        super.clear(format);
        _currentdataset = -1;
        int size = _points.size();
        _points = new Vector();
        _prevx = new Vector();
        _prevy = new Vector();
        _painted = false;
        _maxdataset = -1;
        _firstinset = true;
        _sawfirstdataset = false;
        _pxgraphBlankLineMode = true;
        _endian = _NATIVE_ENDIAN;
        _xyInvalid = false;
        _filename = null;
        _showing = false;
        if (format) {
            _formats = new Vector();
            _marks = 0;
            _pointsPersistence = 0;
            _sweepsPersistence = 0;
            _bars = false;
            _barwidth = 0.5;
            _baroffset = 0.05;
            _connected = true;
            _impulses = false;
            _reusedatasets = false;
        }
    }

    /** Erase the point at the given index in the given dataset.  If
     *  lines are being drawn, also erase the line to the next points
     *  (note: not to the previous point).  The point is not checked to
     *  see whether it is in range, so care must be taken by the caller
     *  to ensure that it is.
     *  The change will be made visible if the plot is visible
     *  on the screen.  Otherwise, it will take effect the next time the plot
     *  is drawn on the screen.
     *
     *  @param dataset The data set index.
     *  @param index The index of the point to erase.
     */
    public synchronized void erasePoint(int dataset, int index) {
        _checkDatasetIndex(dataset);
        if (isShowing()) {
            _erasePoint(getGraphics(), dataset, index);
        }
        Vector points = (Vector) _points.elementAt(dataset);
        if (points != null) {
            PlotPoint pt = (PlotPoint) points.elementAt(index);
            if (pt != null) {
                if (pt.x == _xBottom || pt.x == _xTop || pt.y == _yBottom || pt.y == _yTop) {
                    _xyInvalid = true;
                }
                points.removeElementAt(index);
            }
        }
    }

    /** Rescale so that the data that is currently plotted just fits.
     *  This overrides the base class method to ensure that the protected
     *  variables _xBottom, _xTop, _yBottom, and _yTop are valid.
     *  This method calls repaint(), which eventually causes the display
     *  to be updated.
     */
    public synchronized void fillPlot() {
        if (_xyInvalid) {
            _xBottom = Double.MAX_VALUE;
            _xTop = -Double.MIN_VALUE;
            _yBottom = Double.MAX_VALUE;
            _yTop = -Double.MIN_VALUE;
            for (int dataset = 0; dataset < _points.size(); dataset++) {
                Vector points = (Vector) _points.elementAt(dataset);
                for (int index = 0; index < points.size(); index++) {
                    PlotPoint pt = (PlotPoint) points.elementAt(index);
                    if (pt.x < _xBottom) _xBottom = pt.x;
                    if (pt.x > _xTop) _xTop = pt.x;
                    if (pt.y < _yBottom) _yBottom = pt.y;
                    if (pt.y > _yTop) _yTop = pt.y;
                }
            }
        }
        _xyInvalid = false;
        if (_bars) {
            if (_yBottom > 0.0) _yBottom = 0.0;
            if (_yTop < 0.0) _yTop = 0.0;
        }
        super.fillPlot();
    }

    /** Return the last file name seen on the command-line arguments parsed
     *  by parseArgs().  If there was none, return null.
     *  @return A file name, or null if there is none.
     */
    public String getCmdLineFilename() {
        return _filename;
    }

    /** Return the maximum number of data sets.
     *  This method is deprecated, since there is no longer an upper bound.
     *  @deprecated
     */
    public int getMaxDataSets() {
        return Integer.MAX_VALUE;
    }

    /** Start a new thread to paint the component contents.
     *  This is done in a new thread so that large data sets can
     *  can be displayed without freezing the user interface.
     *  When repainting is completed, the protected variable _painted
     *  is set to true, and notifyAll() is called.
