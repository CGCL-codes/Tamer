package uk.co.ordnancesurvey.rabbitgui.parserproblem.list;

import org.antlr.stringtemplate.StringTemplate;
import uk.ac.leeds.comp.ui.base.UIModel;
import uk.ac.leeds.comp.ui.factory.UIModelFactory;
import uk.co.ordnancesurvey.rabbitgui.RbtTextKey;
import uk.co.ordnancesurvey.rabbitgui.RbtTextKeyAttributes;
import uk.co.ordnancesurvey.rabbitgui.document.RbtDocumentModel;
import uk.co.ordnancesurvey.rabbitgui.parserproblem.ErrorAndWarningParsedResultVisitor;
import uk.co.ordnancesurvey.rabbitparser.IRabbitMessage;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedPart;

/**
 * This model contains a list of warnings retrieved from a
 * {@link RbtDocumentModel}
 * 
 * @author rdenaux
 * 
 */
public class RbtParserWarningListFromDocumentModelImpl extends RbtParserProblemListModelImpl {

    private static final long serialVersionUID = 3767204962165345279L;

    private final RbtDocumentModel sourceDocModel;

    private final WarningListener warningListener;

    public RbtParserWarningListFromDocumentModelImpl(RbtDocumentModel aSourceDocModel, UIModelFactory aUIModelFactory) {
        super(aUIModelFactory);
        sourceDocModel = aSourceDocModel;
        ALLOWS_REPEATED_MODELOBJECTS = true;
        warningListener = new WarningListener(sourceDocModel);
    }

    /**
	 * Listens to the {@link RbtDocumentModel} and traverses it looking for
	 * warnings and add them to this
	 * {@link RbtParserWarningListFromDocumentModelImpl}
	 * 
	 * @author rdenaux
	 * 
	 */
    private class WarningListener extends ErrorAndWarningParsedResultVisitor {

        public WarningListener(RbtDocumentModel aDocModel) {
            super(aDocModel);
        }

        private static final long serialVersionUID = -5890478294248388596L;

        @Override
        protected void handleErrors(IParsedPart sentencePart) {
        }

        @Override
        protected void handleWarnings(IParsedPart sentencePart) {
            addWarningsAsItems(sentencePart);
        }

        @Override
        protected void doAfterTransversing() {
            notifyUpdate(UIModel.MODEL_REFRESHED);
        }

        @Override
        protected void doBeforeTransversing() {
            clearItems();
        }
    }

    @Override
    public void dispose() {
        warningListener.dispose();
        super.dispose();
    }

    /**
	 * Adds items based on the errors in aSentencePart.
	 * 
	 * @param aSentencePart
	 */
    public void addWarningsAsItems(IParsedPart aSentencePart) {
        for (final IRabbitMessage warnMsg : aSentencePart.getWarnings()) {
            addItem(createItemForObject(warnMsg));
        }
    }

    /**
	 * The default name for this
	 * 
	 * @see uk.ac.leeds.comp.ui.itemlist.impl.ItemListModelImpl#getShortListName()
	 */
    @Override
    public String getShortListName() {
        final StringTemplate st = RbtTextKey.RABBIT_WARNINGS_WITH_NUMBER.getAsTemplate();
        st.setAttribute(RbtTextKeyAttributes.number.name, getUnmodifiableItemList().size());
        return st.toString();
    }
}
