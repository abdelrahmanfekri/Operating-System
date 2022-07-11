
public class Type {
    String stringData;

    public Type(String stringData) {
        this.stringData = stringData;
    }


    public int getIntData() {
        return Integer.parseInt(stringData);
    }

    public String getStringData() {
        return stringData;
    }


    public String toString(){
        return stringData;
    }

}
