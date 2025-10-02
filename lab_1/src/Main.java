import java.util.ArrayList;

class NumberDifference{

    ArrayList<Integer> a = new ArrayList<>();
    ArrayList<Integer> b = new ArrayList<>();

    NumberDifference(ArrayList<Integer> a, ArrayList<Integer> b){
        this.a = a;
        this.b = b;
    }
    int difference;


    int getProduct(){
        int product = 1;
        for(int i = 0; i < a.size(); i++){
            product *= a.get(i);
        }
        return product;
    }
    int getProduct2(){
        int product = 1;
        for(int i = 0; i < b.size(); i++){
            product *= b.get(i);
        }
        return product;
    }
    int getDifference(){
        return getProduct() - getProduct2();
    }
}

