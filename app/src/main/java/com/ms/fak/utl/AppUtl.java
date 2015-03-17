package com.ms.fak.utl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppUtl {
    public final static Locale loc = new Locale("sr", "RS");
    //public final static Locale loc = new Locale("fr", "FR");
    public final static NumberFormat nf = NumberFormat.getNumberInstance(loc);
    public final static DecimalFormat fmtIznos = (DecimalFormat) nf;

    public final static String fmtDate = "dd.MM.yyyy";
    public final static SimpleDateFormat sdf = new SimpleDateFormat(fmtDate);

    public final static DecimalFormat fmtBroj = new DecimalFormat("00000");
    public final static DecimalFormat fmtCeo = new DecimalFormat("#,##0");

    static {
        fmtIznos.applyPattern("#,##0.00");
        fmtIznos.setParseBigDecimal(true);
        sdf.setLenient(false);
    }

    public static String formatIznos(BigDecimal iznos) {
        return fmtIznos.format(iznos);
    }

    public static String getDatum(Date date) {
        String result = "??";
        try {
            result = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Date getDanas() {
        Date datum = new Date();
        try {
            // datum bez time!
            datum = sdf.parse(sdf.format(datum));
        } catch (ParseException ex) {
            Logger.getLogger(AppUtl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return datum;
    }

    public static void gasiKeyb(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String HTML_MIME_TYPE = "text/html";

    public static final void openPdf(Context context, Uri localUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(localUri, PDF_MIME_TYPE);
        context.startActivity(intent);
    }

    public static boolean isPdfSupported(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        final File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS ), "test.pdf");
        intent.setDataAndType(Uri.fromFile(tempFile), PDF_MIME_TYPE);
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public static String iznosSlovima(BigDecimal broj) {
        if (broj.compareTo(BigDecimal.ZERO) == 0) {
            return "nula dinara i 00/100";
        }
        String cbroj;
        String rbroj;
        String sbroj = broj.toPlainString();
        int punkt = sbroj.indexOf('.');
        if (punkt < 0) {
            cbroj = sbroj;
            rbroj = "00";
        } else {
            cbroj = sbroj.substring(0, punkt);
            rbroj = sbroj.substring(++punkt, ++punkt + 1);
        }
        if (cbroj.length() > 15) {
            return "???";
        }
        //ceo broj u obliku stringa dopunjen do 16 cifara nulama
        String nule = ""; //dopuna nulama
        for (int i = 0; i < 15 - cbroj.length(); i++) {
            nule += "0";
        }
        cbroj = nule + cbroj;
        String zadnja = cbroj.substring(14);

        String pare = (zadnja.equals("1") ? " dinar" : " dinara") + " i " + rbroj + "/100";

        StringBuilder result = new StringBuilder();

        String[] imebr = {"nula", "jedan", "dva", "tri", "četiri", "pet", "šest", "sedam", "osam", "devet"};

        for (int i = 1; i < 15; i += 3) {
            String tric = cbroj.substring(i - 1, i + 2); //trojka brojeva
            int trojka = Integer.parseInt(tric);

            if (!tric.equals("000")) {
                int cs = Integer.parseInt(tric.substring(0, 1)); //cifra stotina
                int cd = Integer.parseInt(tric.substring(1, 2)); //cifra desetica
                int cj = Integer.parseInt(tric.substring(2, 3)); //cifra jedinica

                switch (cs) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        result.append("dve");
                        break;
                    default:
                        result.append(imebr[cs]);
                        break;
                }

                switch (cs) {
                    case 0:
                        break;
                    case 1:
                        result.append("sto");
                        break;
                    case 2:
                    case 3:
                        result.append("sta");
                        break;
                    case 4:
                        result.append("sto");
                        break;
                    default:
                        result.append("sto");
                        break;
                }

                String sl1 = "";
                if (cj != 0) {
                    sl1 = imebr[cj];
                }

                switch (cd) {
                    case 4:
                        result.append("četr");
                        break;
                    case 6:
                        result.append("šez");
                        break;
                    case 5:
                        result.append("pe");
                        break;
                    case 9:
                        result.append("deve");
                        break;
                    case 2:
                    case 3:
                    case 7:
                    case 8:
                        result.append(imebr[cd]);
                        break;
                    case 1:
                        sl1 = "";
                        switch (cj) {
                            case 0:
                                result.append("deset");
                                break;
                            case 1:
                                result.append("jeda");
                                break;
                            case 4:
                                result.append("četr");
                                break;
                            default:
                                result.append(imebr[cj]);
                                break;
                        }
                        if (cj > 0) {
                            result.append("naest");
                        }
                        break;
                }

                if (cd > 1) {
                    result.append("deset");
                }

                if ((i == 4 || i == 10) && (cd != 1)) {
                    if (cj == 1) {
                        sl1 = "jedna";
                    } else if (cj == 2) {
                        sl1 = "dve";
                    }
                }

                result.append(sl1);

                switch (i) {
                    case 1:
                        result.append("bilion");
                        if (cj > 1 || cd == 1) {
                            result.append("a");
                        }
                        break;
                    case 4:
                        result.append("milijard");
                        if ((trojka % 100 > 11) && (trojka % 100 < 19)) {
                            result.append("i");
                        } else if (cj == 1) {
                            result.append("a");
                        } else if (cj > 4 || cj == 0) {
                            result.append("i");
                        } else if (cj > 1) {
                            result.append("e");
                        }
                        break;
                    case 7:
                        result.append("milion");
                        if ((trojka % 100 > 11) && (trojka % 100 < 19) || (cj != 1)) {
                            result.append("a");
                        }
                        break;
                    case 10:
                        result.append("hiljad");
                        if ((trojka % 100 > 11) && (trojka % 100 < 19) || (cj == 1)) {
                            result.append("a");
                        } else if (trojka == 1) {
                            result.append("u");
                        } else if (cj > 4 || cj == 0) {
                            result.append("a");
                        } else if (cj > 1) {
                            result.append("e");
                        }
                }
            }
        }
        return result + pare;
    }

}
