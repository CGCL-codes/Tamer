package ucm;

import view.ManagerView;

/**
 *
 * @authors LianMing, FengChu
 */
public class UCDisplayManagerView implements UCController {

    public UCDisplayManagerView() {
    }

    public boolean run() {
        ManagerView view = new ManagerView();
        view.setVisible(true);
        return true;
    }
}
