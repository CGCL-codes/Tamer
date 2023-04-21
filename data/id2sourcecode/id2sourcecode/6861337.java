    private void dessineCercleTmp(final TraceLigne _trace, final int _xi, final int _yi, final int _signx, final int _signy, final int _cote) {
        final int xf = _xi + _signx * _cote;
        final int yf = _yi + _signy * _cote;
        final int xm = (_xi + xf) / 2;
        final int ym = (_yi + yf) / 2;
        final int vxm = (int) ((xf - _xi) * DeEllipse.C_MAGIC);
        final int vym = (int) ((yf - _yi) * DeEllipse.C_MAGIC);
        final Graphics2D g = (Graphics2D) calque_.getGraphics();
        _trace.dessineArc(g, xm, _yi, xf, ym, vxm, 0, 0, vym);
        _trace.dessineArc(g, xf, ym, xm, yf, 0, vym, -vxm, 0);
        _trace.dessineArc(g, xm, yf, _xi, ym, -vxm, 0, 0, -vym);
        _trace.dessineArc(g, _xi, ym, xm, _yi, 0, -vym, vxm, 0);
    }
