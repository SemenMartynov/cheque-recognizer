/**
 * Classname:
 * User: dimatwl
 * Date: 4/19/12
 * Time: 4:08 PM
 */
public class NoConnectionException extends Exception{
    @Override
    public String getMessage(){
        return "Can't establish connection.";
    }
}
