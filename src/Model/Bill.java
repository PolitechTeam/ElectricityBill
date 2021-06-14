package Model;

import java.util.Date;

public class Bill {
    private int id;
    private int userId;
    private int indication;
    private Date paymentDate;

    public Bill(int id, int userId, int indication, Date paymentDate) {
        this.id = id;
        this.userId = userId;
        this.indication = indication;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIndication() {
        return indication;
    }

    public void setIndication(int indication) {
        this.indication = indication;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
