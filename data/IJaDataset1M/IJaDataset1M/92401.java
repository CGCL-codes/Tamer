package org.isistan.flabot.util.edition;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractCompositeEditionItem<T> extends AbstractEditionItem<T> implements CompositeEditionItem<T> {

    private List<EditionItem<T>> editionItems = new LinkedList<EditionItem<T>>();

    private EditionItemChangeListener<T> listenerAdapter = new EditionItemChangeListener<T>() {

        public void changed(EditionItem notifier, EditionItem originator) {
            AbstractCompositeEditionItem.this.update(notifier, originator);
        }
    };

    private T element = null;

    public boolean addEditionItem(EditionItem<T> editionItem) {
        if (editionItems.contains(editionItem)) return false;
        editionItems.add(editionItem);
        editionItem.addListener(listenerAdapter);
        if (element != null) {
            initializeChild(editionItem);
        }
        return true;
    }

    private void initializeChild(EditionItem<T> editionItem) {
        if (!editionItem.accepts(element)) {
            disposeChild(editionItem);
            editionItems.remove(editionItem);
        } else {
            editionItem.initialize(getChildContainer(), element);
        }
    }

    public boolean removeEditionItem(EditionItem<T> editionItem) {
        boolean removed = editionItems.remove(editionItem);
        if (removed) {
            editionItem.removeListener(listenerAdapter);
            editionItem.dispose();
        }
        return removed;
    }

    public void initialize(Composite container, T element) {
        this.element = element;
        internalInitialize(container, element);
        List<EditionItem<T>> copy = new LinkedList<EditionItem<T>>();
        copy.addAll(editionItems);
        for (EditionItem<T> editionItem : copy) {
            initializeChild(editionItem);
        }
        postChildInitialization(editionItems.toArray(new EditionItem[editionItems.size()]));
    }

    protected abstract void internalInitialize(Composite container, T element);

    protected abstract void postChildInitialization(EditionItem<T>[] editionItems);

    protected abstract String getCommandName();

    public Command getCommand() {
        CompoundCommand command = new CompoundCommand(getCommandName());
        for (EditionItem<T> editionItem : editionItems) {
            Command childCommand = editionItem.getCommand();
            if (childCommand != null) command.add(childCommand);
        }
        if (command.size() == 0) return null; else return command;
    }

    public boolean canCreateCommand() {
        for (EditionItem<T> editionItem : editionItems) {
            if (!editionItem.canCreateCommand()) {
                return false;
            }
        }
        return true;
    }

    public EditionItemStatus getStatus() {
        if (editionItems.size() == 0) {
            return EditionItemStatus.DEFAULT_OK;
        }
        EditionItemStatus[] statuses = new EditionItemStatus[editionItems.size()];
        int index = 0;
        for (EditionItem<T> editionItem : editionItems) {
            statuses[index++] = editionItem.getStatus();
        }
        return new CompositeEditionItemStatus(statuses);
    }

    private void update(EditionItem<? extends T> editionItem, EditionItem<? extends T> originator) {
        notifyChange(originator);
    }

    public void dispose() {
        for (EditionItem<T> editionItem : editionItems) {
            disposeChild(editionItem);
        }
    }

    public void disposeChild(EditionItem<T> editionItem) {
        editionItem.removeListener(listenerAdapter);
        editionItem.dispose();
    }

    public boolean accepts(T element) {
        return true;
    }
}
