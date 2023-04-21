package com.jw.mos.gui.components;

import com.jw.mos.gui.components.support.IDialogPropertyEditable;
import com.jw.mos.gui.components.support.IPropertyEditable;
import com.jw.mos.gui.components.support.mouse.PropertyEditorMouseSupport;
import com.jw.mos.gui.data.IDataProvidable;
import com.jw.mos.gui.data.OwnResourceData;

/**
 * @Title Resource.
 * @Description Abstract resource representation. 
 * Manages resource's parent process, data and it's tooltip text.
 * @Author Jacek Wisniewski
 * @Date 2008-06-18 22:14:43
 */
public abstract class Resource<T extends OwnResourceData> extends ResourceSymbol implements IDataProvidable, IPropertyEditable<Resource<T>> {

    private final Process parentProcess;

    /**Component data*/
    private final T data;

    public Resource(Process parentProcess, int capacity, int number) {
        super();
        this.parentProcess = parentProcess;
        data = getResourceData(capacity, number);
        init();
    }

    private void init() {
        switchDnDSupport(false);
        setToolTipText(getTooltip());
        addPropertyEditorMouseSupport();
    }

    protected abstract T getResourceData(int capacity, int number);

    protected abstract String getTooltip();

    /********************************************  {@link IDialogPropertyEditable} **************************/
    @Override
    public void addPropertyEditorMouseSupport() {
        addMouseListener(new PropertyEditorMouseSupport());
    }

    /********************************************  {@link IDataProvidable} **************************/
    @Override
    public T getData() {
        return data;
    }

    /************************************************************************************************/
    public Process getParentProcess() {
        return parentProcess;
    }

    public void setNumber(int number) {
        data.setNumber(number);
        setToolTipText(getTooltip());
    }
}
