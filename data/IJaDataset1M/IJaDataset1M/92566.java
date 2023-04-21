package cb.recommender.input.dataManipulator;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import cb.recommender.base.input.bean.CBInputBean;
import cb.recommender.base.luceneUtils.LuceneAnalyzer;
import cb.recommender.base.luceneUtils.LuceneIndex;
import cb.recommender.base.recommender.IdealizeDocument;
import cb.recommender.base.recommender.OperationDocument;
import cb.recommender.base.recommender.IdealizeDocument.FieldType;
import com.uplexis.idealize.base.exceptions.IdealizeCoreException;
import com.uplexis.idealize.base.exceptions.IdealizeInputException;
import com.uplexis.idealize.base.exceptions.IdealizeUnavailableResourceException;
import com.uplexis.idealize.hotspots.datamanipulator.DataManipulator;
import com.uplexis.idealize.hotspots.input.bean.InputBean;

/**
 * Does the data update methods for content-based recommendations.
 * 
 * @author Alex Amorim Dutra
 * 
 */
public class CBDataManipulator extends DataManipulator {

    /**
	 * Lucene index to be changed and that is bound to the datamodel.
	 */
    private LuceneIndex lucene;

    /**
	 * Represents a quantity of items to be stored, without even changing the
	 * DataModel and the lucene index.
	 */
    private int cont;

    /**
	 * List where the documents are stored before changing the datamodel and
	 * lucene.
	 */
    private List<OperationDocument> operationDocs;

    public final char OP_INSERT = 'i';

    public final char OP_UPDATE = 'u';

    public final char OP_REMOVE = 'r';

    /**
	 * Class constructor
	 * 
	 * @throws IdealizeInputException
	 *             thrown when the input contains invalid data.
	 */
    public CBDataManipulator() throws IdealizeInputException {
        this.operationDocs = new LinkedList<OperationDocument>();
        cont = 0;
    }

    /**
	 * Getter list containing the documents to be subsequently stored in the
	 * data model and lucene.
	 * 
	 * @return list contain KeyDocument
	 */
    public List<OperationDocument> getKeyDocs() {
        return operationDocs;
    }

    /**
	 * Setter the list keyDocument.
	 * 
	 * @param operationDocs
	 *            list containing OperationDocument
	 */
    public void setDocs(List<OperationDocument> operationDocs) {
        this.operationDocs = operationDocs;
    }

    /**
	 * Getter the quantity of items that will reach to the change in data model.
	 * 
	 * @return cont integer counter that indicates how many documents were
	 *         altered.
	 */
    public int getCont() {
        return cont;
    }

    /**
	 * Setter the quantity of items that will reach to the change in data model.
	 * 
	 * @param cont
	 *            integer counter that indicates how many documents were
	 *            altered.
	 */
    public void setCont(int cont) {
        this.cont = cont;
    }

    @Override
    public boolean insertItem(InputBean item) throws IdealizeInputException, IdealizeUnavailableResourceException, IdealizeCoreException {
        OperationDocument keyDoc = new OperationDocument(getDocumentOf(item), this.OP_INSERT);
        if (this.operationDocs.add(keyDoc)) {
            cont++;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeItem(InputBean item) throws IdealizeInputException, IdealizeUnavailableResourceException, IdealizeCoreException {
        OperationDocument keyDoc = new OperationDocument(getDocumentOf(item), this.OP_REMOVE);
        if (this.operationDocs.add(keyDoc)) {
            cont++;
            return true;
        }
        return false;
    }

    @Override
    public boolean updateItem(InputBean item) throws IdealizeInputException, IdealizeUnavailableResourceException, IdealizeCoreException {
        OperationDocument keyDoc = new OperationDocument(getDocumentOf(item), this.OP_UPDATE);
        if (this.operationDocs.add(keyDoc)) {
            cont++;
            return true;
        }
        return false;
    }

    /**
	 * Updates the DataModel and lucene when you reach the amount of items
	 * defined in cont.
	 * 
	 * @param keyDocs
	 *            List containing KeyDocument
	 * @throws IdealizeInputException
	 *             thrown when problems with updates of documents in lucene
	 * @throws IOException
	 *             thrown when problems with directory lucene
	 */
    public void updateLucene(List<OperationDocument> opDocs) throws IdealizeInputException, IOException {
        IndexReader reader = IndexReader.open(lucene.getDirectory());
        Analyzer analyzer = LuceneAnalyzer.getAnalyzer(LuceneAnalyzer.AnalyzerType.MIXED);
        for (int i = 0; i < opDocs.size(); i++) {
            char operation = opDocs.get(i).getOperation();
            if (operation == this.OP_INSERT) lucene.addDoc(opDocs.get(i).getDocument(), analyzer); else if (operation == this.OP_UPDATE) lucene.updateDoc(opDocs.get(i).getDocument(), analyzer, reader); else lucene.deleteDoc(opDocs.get(i).getDocument(), analyzer, reader);
        }
        opDocs.clear();
    }

    /**
	 * Gets the document that represent an InputBean.
	 * 
	 * @param item
	 *            The InputItem.
	 * @return Document The document that represents the item.
	 */
    private Document getDocumentOf(InputBean item) {
        CBInputBean cbItem = (CBInputBean) item;
        IdealizeDocument doc = new IdealizeDocument(String.valueOf(cbItem.getItemId()));
        Iterator<String> keys = cbItem.getNames();
        String key;
        String[] values;
        FieldType type;
        String value;
        while (keys.hasNext()) {
            key = keys.next();
            if (!key.equals("id")) {
                values = cbItem.getValues(key);
                type = cbItem.getType(key);
                value = "";
                for (int i = 0; i < values.length; i++) {
                    value += values[i];
                    if (i != values.length - 1) {
                        value += "\n";
                    }
                }
                doc.setField(key, value, type);
            }
        }
        return doc.getDocument();
    }

    @Override
    public boolean insertUser(InputBean item) throws IdealizeInputException, IdealizeUnavailableResourceException, IdealizeCoreException, IdealizeInputException {
        return false;
    }

    @Override
    public boolean removeUser(InputBean item) throws IdealizeInputException, IdealizeUnavailableResourceException, IdealizeCoreException, IdealizeInputException {
        return false;
    }

    @Override
    public boolean updateUser(InputBean item) throws IdealizeInputException, IdealizeUnavailableResourceException, IdealizeCoreException, IdealizeInputException {
        return false;
    }
}
