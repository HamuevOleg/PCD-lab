import java.util.ArrayList;

public class TurnManager {
    int currentTurn;
    int turnIndex;
    ArrayList<Integer> sequence;


    public TurnManager(ArrayList<Integer> sequence) {
        this.sequence = sequence;
        if (sequence != null && !sequence.isEmpty())
            this.currentTurn = sequence.get(turnIndex);
        else
            this.currentTurn = -1;

    }

    public synchronized void waitForTurn(int myId) throws InterruptedException {
        // check for spurious wakeups
        while (currentTurn != myId) {
            wait();
        }
    }

    public synchronized void nextTurn() {
        //logic : 2 -> 4 -> 1 -> 3
        turnIndex++;
        if (turnIndex < sequence.size())
            this.currentTurn = sequence.get(turnIndex);
        else
            this.currentTurn = -1;
        notifyAll();
    }
}