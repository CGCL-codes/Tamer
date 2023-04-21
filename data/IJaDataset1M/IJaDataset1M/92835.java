package com.google.gdt.eclipse.core.ui;

import com.google.gdt.eclipse.core.sdk.Sdk;
import org.eclipse.swt.widgets.Composite;

/**
 * An Sdk selection block whose default is relative to the workspace.
 * 
 * @param <T> type of sdk
 */
public abstract class WorkspaceSdkSelectionBlock<T extends Sdk> extends SdkSelectionBlock<T> {

    public WorkspaceSdkSelectionBlock(Composite parent, int style) {
        super(parent, style);
        updateSdkBlockControls();
    }
}
