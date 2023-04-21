package info.walnutstreet.vs.ps03.client.view.listener;

import info.walnutstreet.vs.ps03.client.controller.ConnectionController;
import info.walnutstreet.vs.ps03.client.controller.UiUpdateController;
import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * @author Christoph Gostner
 * @version 0.1
 *
 */
public class BuyMyGoodsSelectionListener implements SelectionListener {

    /**
	 * Logging ...
	 */
    private static Logger logger = Logger.getLogger(BuyMyGoodsSelectionListener.class);

    @Override
    public void widgetDefaultSelected(SelectionEvent event) {
        this.widgetSelected(event);
    }

    @Override
    public void widgetSelected(SelectionEvent event) {
        BuyMyGoodsSelectionListener.logger.debug("Listener: Finally buy all the selected goods");
        UiUpdateController controller2 = UiUpdateController.getInstance();
        ConnectionController.getInstance().buyGoods();
        controller2.rebuildClientListTable();
    }
}
