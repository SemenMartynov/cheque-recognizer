package ru.spbau.cheque.server.ofx;

/**
 * NetBeans
 *
 * @author sam
 */
public class ExportOFX {

    public static void main(String[] args) {
        int count = 5;

        String system_time = new java.text.SimpleDateFormat("YmdHMS").format(java.util.Calendar.getInstance().getTime());
        String balance_time = new java.text.SimpleDateFormat("yyyyMMdd").format(java.util.Calendar.getInstance().getTime());

        //PrintWriter out = new PrintWriter(new FileOutputStream("results.ofx"));

        System.out.println("<OFX><SIGNONMSGSRSV1><SONRS><STATUS><CODE>0<SEVERITY>INFO</STATUS><DTSERVER>" + system_time + "<LANGUAGE>RUS<DTACCTUP>" + system_time + "<FI><ORG>CHEQUERECOGNIZER<FID>01234</FI></SONRS></SIGNONMSGSRSV1>");

        System.out.println("<BANKMSGSRSV1><STMTTRNRS><TRNUID>23382938<STATUS><CODE>0<SEVERITY>INFO</STATUS><STMTRS><CURDEF>RUR<BANKACCTFROM><BANKID>000000000<ACCTID>CASH<ACCTTYPE>SAVINGS</BANKACCTFROM>");

        for (int i = 1; i <= count; i++) {
            System.out.println("<BANKTRANLIST><DTSTART>20070101<DTEND>20071015<STMTTRN><TRNTYPE>CREDIT<DTPOSTED>" + balance_time + "<DTUSER>" + balance_time + "<TRNAMT>200,00<FITID>" + i + "<NAME>TOVAR" + i + "<MEMO>MAGAZIN</STMTTRN></BANKTRANLIST>");
        }
        System.out.println("</STMTRS></STMTTRNRS></BANKMSGSRSV1></OFX>");
    }
}