package imgrecognition;

public class InvalidQRException extends Exception {

    public InvalidQRException(String errorMessage) {
        super("Invalid QR code reading! " + errorMessage);
    }

}
