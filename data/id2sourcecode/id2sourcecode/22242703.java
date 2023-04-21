    protected void saveConfig() {
        TGConfigManager config = TuxGuitar.instance().getConfig();
        config.setProperty(TGConfigKeys.LAYOUT_MODE, getEditor().getTablature().getViewLayout().getMode());
        config.setProperty(TGConfigKeys.LAYOUT_STYLE, getEditor().getTablature().getViewLayout().getStyle());
        config.setProperty(TGConfigKeys.SHOW_PIANO, !TuxGuitar.instance().getPianoEditor().isDisposed());
        config.setProperty(TGConfigKeys.SHOW_MATRIX, !TuxGuitar.instance().getMatrixEditor().isDisposed());
        config.setProperty(TGConfigKeys.SHOW_FRETBOARD, TuxGuitar.instance().getFretBoardEditor().isVisible());
        config.setProperty(TGConfigKeys.SHOW_INSTRUMENTS, !TuxGuitar.instance().getChannelManager().isDisposed());
        config.setProperty(TGConfigKeys.SHOW_TRANSPORT, !TuxGuitar.instance().getTransport().isDisposed());
        config.setProperty(TGConfigKeys.SHOW_MARKERS, !MarkerList.instance().isDisposed());
        config.setProperty(TGConfigKeys.MAXIMIZED, TuxGuitar.instance().getShell().getMaximized());
        config.setProperty(TGConfigKeys.WIDTH, TuxGuitar.instance().getShell().getClientArea().width);
        config.setProperty(TGConfigKeys.HEIGHT, TuxGuitar.instance().getShell().getClientArea().height);
        config.setProperty(TGConfigKeys.EDITOR_MOUSE_MODE, getEditor().getTablature().getEditorKit().getMouseMode());
        config.setProperty(TGConfigKeys.MATRIX_GRIDS, TuxGuitar.instance().getMatrixEditor().getGrids());
        TuxGuitar.instance().getConfig().save();
    }
