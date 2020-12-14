package SaperPackage;

public class Converter {
    private final static Converter instance = new Converter();

    private Converter(){}

    public static Converter getInstance(){
        return instance;
    }

    public String convertLongToTimeString(Long seconds){
        String minutes, second, result;
        minutes = null;
        second = null;
        long temp = seconds;
        if(temp >= 60){
            if((temp / 60) < 10){
                minutes = "0" + Long.toString(temp / 60);
            }else {
                minutes = Long.toString(temp / 60);
            }
            temp = temp % 60;
        }
        if(temp < 10){
            second = "0" + Long.toString(temp);
        }else{
            second = Long.toString(temp);
        }
        if(minutes == null){
            minutes = "00";
        }
        result = minutes + ":" + second;
        return result;
    }
}
