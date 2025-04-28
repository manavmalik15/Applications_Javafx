package org.connect4;
import java.io.Serializable;

public class Communication implements Serializable {
    private static final long serialVersionUID = 42L;
    public String sender;
    public String receiver;
    public int whatIsIt;
    public String message;

    // Constants for request types
    public static final int CREATE_ACCOUNT = 0;
    public static final int LOGIN = 1;
    public static final int REQUEST_ONLINE_PLAYERS = 2;
    public static final int START_RANDOM_MATCH = 3;
    public static final int REQUEST_PLAY = 4;
    public static final int ADD_FRIEND = 5;
    public static final int MOVE = 6;
    public static final int MESSAGE = 7;
    public static final int END_GAME = 8;
    public static final int BYE = 10;
    public static final int ACCEPT_FRIND  = 11;
    public static final int ACCEPT_GAME  = 12;
    public static final int SUCCESS  = 13;
    public static final int FAILED  = 14;

    // You can keep adding more if needed

    public Communication(String sender, String receiver, int whatIsIt, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.whatIsIt = whatIsIt;
        this.message = message;
    }

    // setters
    public void setSender(String sender) { this.sender = sender; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public void setWhatIsIt(int whatIsIt) { this.whatIsIt = whatIsIt; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "Communication{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", whatIsIt=" + whatIsIt +
                ", message='" + message + '\'' +
                '}';
    }
}
