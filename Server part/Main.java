/**
 * Classname:
 * User: dimatwl
 * Date: 4/19/12
 * Time: 4:49 PM
 */
public class Main {
    public static void main(String[] argv){
        try{
            ChequeRecognitionServer srv = new ChequeRecognitionServer(3843);
            srv.start();
        } catch (NoConnectionException e){
            System.out.println("Shit happens.");
        }
    }
}
