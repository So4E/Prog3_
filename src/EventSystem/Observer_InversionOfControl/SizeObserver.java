package EventSystem.Observer_InversionOfControl;

import administration.AdministrationImpl;

import java.math.BigDecimal;
import java.util.Objects;

public class SizeObserver implements Observer {
    private AdministrationImpl observableAdmin;
    private BigDecimal lastSize;
    private BigDecimal maximumSize;

    public SizeObserver(AdministrationImpl observableAdmin) {
        this.observableAdmin = observableAdmin;
        this.observableAdmin.meldeAn(this);
        this.lastSize = observableAdmin.getTotalSize();
        this.maximumSize = this.observableAdmin.getMaxSizeOfMemory();
    }

    @Override
    public void aktualisiere() {
        BigDecimal newSize = observableAdmin.getTotalSize();
        if (Objects.equals(this.lastSize, newSize)) {
            return;
        }
        //save last size as new size
        this.lastSize = newSize;
        //check if currentSize is bigger than 90 % of maximum storage -> if it is, send warning
        double ninetyPercent = maximumSize.doubleValue() * 0.9;
        if (ninetyPercent > newSize.longValue()) return;
        else {
            System.out.println("Warning!!!!!!!!!!!!!!!\n " +
                    "Current size is more than or equal to 90% of maximum storage.\n" +
                    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

}
