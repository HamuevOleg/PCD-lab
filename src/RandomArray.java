import java.util.ArrayList;
import java.util.Random;
public class RandomArray{
    int bound;
    int size;
    RandomArray(int size, int bound){
        this.size = size;
        this.bound = bound;
    }
    ArrayList<Integer> randomise_value(){
        Random rand = new Random();
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < size; i++){
            list.add(rand.nextInt(bound));
        }
        return list;
    }
}