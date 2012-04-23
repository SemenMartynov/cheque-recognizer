package ru.spbau.cheque;

import android.graphics.Bitmap;
import com.googlecode.tesseract.android.TessBaseAPI;
import ru.spbau.cheque.recognition.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class Recognizer {
    public Recognizer() {
        this.tableExtractor = new RegexTableExtractor();
        this.companyNameExtractor = new CompanyNameExtractor();
    }

    public Cheque doRecognition(Bitmap image,
                                int tableX, int tableY, int tableW, int tableH) throws OcrFailedException, UnsupportedEncodingException {

        File myDir = new File("/sdcard");//getExternalFilesDir(Environment.MEDIA_MOUNTED);
        TessBaseAPI baseApi = new TessBaseAPI();
        //baseApi.setPageSegMode(TessBaseAPI.PSM_SINGLE_BLOCK);

        boolean inited = baseApi.init(myDir.toString(), "rus"/*, TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED*/); // myDir + "/tessdata/eng.traineddata" must be present
        baseApi.setImage(image);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.setImage(Bitmap.createBitmap(image, tableX, tableY, tableW, tableH));
        String recognizedText2 = baseApi.getUTF8Text();
        baseApi.end();

        List<String> ocredText = Arrays.asList(recognizedText.split("\n"));
        List<String> ocredAreaText = Arrays.asList(recognizedText2.split("\n"));
        ////

//        List<String> ocredText = engine.doOcr(image);
//        List<String> ocredAreaText = engine.doOcr(image.getSubimage(tableX, tableY, tableW, tableH));

        ChequeFormat format = determineFormat(ocredText);
        String companyName = companyNameExtractor.extract(format, ocredText);
        List<ru.spbau.cheque.recognition.BlueObject> entries = tableExtractor.extract(format, ocredAreaText);

        return new Cheque(companyName, entries);
    }

    public ChequeFormat determineFormat(List<String> text) {
        //stub
        return new ChequeFormat(companyNameExtractor.extract(new ChequeFormat(""), text));
    }

    private TableExtractor tableExtractor;
    private CompanyNameExtractor companyNameExtractor;
}
