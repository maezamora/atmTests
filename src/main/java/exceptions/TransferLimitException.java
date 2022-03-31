package exceptions;

public class TransferLimitException extends Exception{
    private double limit;

    public TransferLimitException(double limit) {
        this.limit = limit;
    }

    public double getLimit() {
        return this.limit;
    }

    public TransferLimitException(String message) {
        super(message);
    }

}
