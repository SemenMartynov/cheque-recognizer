package ru.spbau.cheque.server.ofx;

import ru.spbau.cheque.server.ofx.SqliteDB;
import java.util.List;

/**
 * NetBeans
 *
 * @author sam
 */
public class ExportOFX {

    public ExportOFX(int userId) {
        SqliteDB DB = new SqliteDB();

        String time = new java.text.SimpleDateFormat("YmdHMS").format(java.util.Calendar.getInstance().getTime());
        OFXFile = "OFXHEADER:100\r\nDATA:OFXSGML\r\nVERSION:103\r\nSECURITY:NONE\r\nENCODING:UTF-8\r\nCHARSET:NONE\r\nCOMPRESSION:NONE\r\nOLDFILEUID:NONE\r\nNEWFILEUID:NONE\r\n<OFX>\r\n\t<SIGNONMSGSRSV1>\r\n\t\t<SONRS>\r\n\t\t\t<STATUS>\r\n\t\t\t\t<CODE>0\r\n\t\t\t\t<SEVERITY>INFO\r\n\t\t\t</STATUS>\r\n\t\t\t<DTSERVER>" + time + "\r\n\t\t\t<LANGUAGE>\r\n\t\t\t<DTACCTUP>" + time + "\r\n\t\t\t<FI>\r\n\t\t\t\t<ORG>CHEQUERECOGNIZER\r\n\t\t\t\t<FID>01234\r\n\t\t\t</FI>\r\n\t\t</SONRS>\r\n\t</SIGNONMSGSRSV1>\r\n\t<BANKMSGSRSV1>\r\n\t\t<STMTTRNRS>\r\n\t\t\t<TRNUID>23382938\r\n\t\t\t<STATUS>\r\n\t\t\t\t<CODE>0\r\n\t\t\t\t<SEVERITY>INFO\r\n\t\t\t</STATUS>\r\n\t\t\t<STMTRS>\r\n\t\t\t\t<CURDEF>RUR\r\n\t\t\t\t<BANKACCTFROM>\r\n\t\t\t\t\t<BANKID>000000000\r\n\t\t\t\t\t<ACCTID>CASH\r\n\t\t\t\t\t<ACCTTYPE>SAVINGS\r\n\t\t\t\t</BANKACCTFROM>\r\n";

        for (ChequeString element : DB.getChequeStrings(userId)) {
            OFXFile += "\t\t\t\t<BANKTRANLIST>\r\n\t\t\t\t\t<DTSTART>20100101\r\n\t\t\t\t\t<DTEND>20101015\r\n\t\t\t\t\t<STMTTRN>\r\n\t\t\t\t\t\t<TRNTYPE>CREDIT\r\n\t\t\t\t\t\t<DTPOSTED>" + element.getOperationTime() + "\r\n\t\t\t\t\t\t<DTUSER>" + element.getOperationTime() + "\r\n\t\t\t\t\t\t<TRNAMT>" + element.getPrice() + "\r\n\t\t\t\t\t\t<FITID>" + element.getId() + "\r\n\t\t\t\t\t\t<NAME>" + element.getName() + "\r\n\t\t\t\t\t\t<MEMO>" + element.getMemo() + "\r\n\t\t\t\t\t</STMTTRN>\r\n\t\t\t\t</BANKTRANLIST>\r\n";
        }
        OFXFile += "\t\t\t</STMTRS>\r\n\t\t</STMTTRNRS>\r\n\t</BANKMSGSRSV1>\r\n</OFX>";
    }
    private String OFXFile;

    @Override
    public String toString() {
        return OFXFile;
    }
}