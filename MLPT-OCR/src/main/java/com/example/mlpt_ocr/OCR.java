package com.example.mlpt_ocr;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.SparseArray;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class OCR {
    public static Uri imageUri;

    public static void takePhoto(Activity x, String app){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(x,
                app + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        x.startActivityForResult(intent, 10);
    }

    public static JSONObject convertText(Activity c, ImageView iv) {
        JSONObject json = new JSONObject();
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(imageUri);
            c.sendBroadcast(mediaScanIntent);
            String hasil = null;
            int targetW = 800;
            int targetH = 800;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(c.getContentResolver().openInputStream(imageUri), null, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            Bitmap bitmap = null;

            bitmap = BitmapFactory.decodeStream(c.getContentResolver().openInputStream(imageUri), null, bmOptions);
            if(iv != null){
                iv.setImageBitmap(bitmap);
            }
            TextRecognizer detector = new TextRecognizer.Builder(c).build();

            if (detector.isOperational() && bitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> textBlocks = detector.detect(frame);
                String blocks = "";
                String lines = "";
                String words = "";

                for (int index = 0; index < textBlocks.size(); index++) {
                    //extract scanned text blocks here
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks = blocks + tBlock.getValue() + ";" + ";";

                    for (Text line : tBlock.getComponents()) {
                        //extract scanned text lines here
                        lines = lines + line.getValue() + ";";
                        for (Text element : line.getComponents()) {
                            //extract scanned text words here
                            words = words + element.getValue() + "; ";
                        }
                    }
                }
                if (textBlocks.size() == 0) {
                    json.put("message", "Scan Failed: Found nothing to scan");
                } else {
                    hasil = lines;
                    String[] arrayStr=hasil.split(";");
                    ArrayList<String> arrayCart= new ArrayList<>();
                    JSONObject jsonS = new JSONObject();
                    JSONObject jsonF = new JSONObject();
//                    System.out.println("jsonS ");
                    for (int a=0; a< arrayStr.length; a++){
//                        System.out.println("arrayStr "+a+". "+arrayStr[a].replace(":",""));
                        if(!arrayStr[a].contains("Nama") &&
                                !arrayStr[a].contains("Tempat") &&
                                !arrayStr[a].contains("Lahir") &&
                                !arrayStr[a].contains("Darah") &&
                                !arrayStr[a].contains("Jenis") &&
                                !arrayStr[a].contains("Alamat") &&
                                !arrayStr[a].contains("Aiamat") &&
                                !arrayStr[a].contains("Alama") &&
                                !arrayStr[a].contains("Aiama") &&
                                !arrayStr[a].contains("Kel/") &&
                                !arrayStr[a].contains("/Des") &&
                                !arrayStr[a].contains("RW") &&
                                !arrayStr[a].contains("Desa") &&
                                !arrayStr[a].contains("matan") &&
                                !arrayStr[a].contains("Agama") &&
                                !arrayStr[a].contains("Status") &&
                                !arrayStr[a].contains("Pekerjaan") &&
                                !arrayStr[a].contains("kerja") &&
                                !arrayStr[a].contains("warga") &&
                                !arrayStr[a].contains("Berlaku") &&
                                !arrayStr[a].contains("Hingga") &&
                                !arrayStr[a].contains("Gol") &&

                                !arrayStr[a].toUpperCase().contains("UDUK") &&
                                !arrayStr[a].toUpperCase().contains("DUK") &&
                                !arrayStr[a].toUpperCase().contains("JERAS") &&
                                !arrayStr[a].toUpperCase().contains("KELAMA") &&
                                !arrayStr[a].toUpperCase().contains("HNGGA") &&
                                !arrayStr[a].toUpperCase().contains("JENES KELAM") &&
                                !arrayStr[a].toUpperCase().contains("UOUK") &&
                                !arrayStr[a].toUpperCase().contains("TUTA0") &&
                                !arrayStr[a].toUpperCase().contains("TUTAO") &&
                                !arrayStr[a].toUpperCase().contains("TEMPA") &&
                                !arrayStr[a].toUpperCase().contains("PERKAWINAN") &&
                                !arrayStr[a].toUpperCase().contains("JENES") &&

                                !arrayStr[a].toUpperCase().equals("ART") &&
                                !arrayStr[a].toUpperCase().equals("KAR") &&
                                !arrayStr[a].toUpperCase().equals("KAR)") &&
                                !arrayStr[a].toUpperCase().equals("K KAR)") &&
                                !arrayStr[a].toUpperCase().equals("KAT") &&
                                !arrayStr[a].toUpperCase().equals("KART") &&
                                !arrayStr[a].toUpperCase().equals("TU TA") &&
                                !arrayStr[a].toUpperCase().equals("TU TAR") &&
                                !arrayStr[a].toUpperCase().equals("TAN") &&
                                !arrayStr[a].toUpperCase().equals("AGAM") &&
                                !arrayStr[a].toUpperCase().equals("AMA") &&
                                !arrayStr[a].toUpperCase().equals("UPUK KAR") &&
                                !arrayStr[a].toUpperCase().equals("UTA") &&
                                !arrayStr[a].toUpperCase().equals("MAMA") &&


                                !(arrayStr[a].length() <3)){
                            System.out.println(arrayStr[a].replace(":",""));
                            arrayCart.add(arrayStr[a].replace(":",""));
//                            jsonS.put("value_"+a, arrayStr[a].replace(":",""));
                        }
                    }

                    for (int b=0; b< arrayCart.size(); b++){
                        jsonS.put("value_"+b, arrayCart.get(b).replace(":","").toUpperCase());
                    }

                    if(arrayStr[2].equals("NIK")){
                        jsonF.put("provinsi", jsonS.getString("value_0"));
                        jsonF.put("kota", jsonS.getString("value_1"));
                        jsonF.put("nik", jsonS.getString("value_3"));
                        jsonF.put("nama", jsonS.getString("value_4"));
                        jsonF.put("lahir", jsonS.getString("value_5"));
                        if (jsonS.getString("value_6").contains("LAK") || jsonS.getString("value_6").contains("PEREM")){
                            String jk = jsonS.getString("value_6");
                            if(jsonS.getString("value_6").contains("LAK")){
                                jk = "LAKI-LAKI";
                            }else if (jsonS.getString("value_6").contains("PEREM")){
                                jk = "PEREMPUAN";
                            }
                            jsonF.put("jk", jk);
                            if(jsonS.getString("value_8").substring(0,3).matches("[0-9]+")){
                                jsonF.put("alamat", jsonS.getString("value_7"));
                                jsonF.put("rtrw", jsonS.getString("value_8"));
                                jsonF.put("keldes", jsonS.getString("value_9"));
                                jsonF.put("kec", jsonS.getString("value_10"));
                                jsonF.put("agama", jsonS.getString("value_11").replace("1","I"));
                                jsonF.put("status", jsonS.getString("value_12"));
//                                jsonF.put("kerja", jsonS.getString("value_13"));
//                                jsonF.put("negara", jsonS.getString("value_14"));

                                if(!jsonS.getString("value_14").contains("WN")){
                                    if(!jsonS.getString("value_15").contains("WN")){
                                        jsonF.put("kerja", jsonS.getString("value_15"));
                                        jsonF.put("negara", jsonS.getString("value_16"));
                                    }else{
                                        jsonF.put("kerja", jsonS.getString("value_14"));
                                        jsonF.put("negara", jsonS.getString("value_15"));
                                    }
                                }else{
                                    jsonF.put("kerja", jsonS.getString("value_13"));
                                    jsonF.put("negara", jsonS.getString("value_14"));
                                }

                            }else {
//                            for (int z=7; z< jsonS.length(); z++){
//
//                            }
                                jsonF.put("alamat", jsonS.getString("value_7")+" "+jsonS.getString("value_8"));
                                jsonF.put("rtrw", jsonS.getString("value_9"));
                                jsonF.put("keldes", jsonS.getString("value_10"));
                                jsonF.put("kec", jsonS.getString("value_11"));
                                jsonF.put("agama", jsonS.getString("value_12").replace("1","I"));
                                jsonF.put("status", jsonS.getString("value_13"));
//                                jsonF.put("kerja", jsonS.getString("value_14"));
//                                jsonF.put("negara", jsonS.getString("value_15"));

                                if(!jsonS.getString("value_15").contains("WN")){
                                    if(!jsonS.getString("value_16").contains("WN")){
                                        jsonF.put("kerja", jsonS.getString("value_16"));
                                        jsonF.put("negara", jsonS.getString("value_17"));
                                    }else{
                                        jsonF.put("kerja", jsonS.getString("value_15"));
                                        jsonF.put("negara", jsonS.getString("value_16"));
                                    }
                                }else{
                                    jsonF.put("kerja", jsonS.getString("value_14"));
                                    jsonF.put("negara", jsonS.getString("value_15"));
                                }
                            }
                        }else{
                            jsonF.put("jk", "-");
                            if(jsonS.getString("value_7").substring(0,3).matches("[0-9]+")){
                                jsonF.put("alamat", jsonS.getString("value_6"));
                                jsonF.put("rtrw", jsonS.getString("value_7"));
                                jsonF.put("keldes", jsonS.getString("value_8"));
                                jsonF.put("kec", jsonS.getString("value_9"));
                                jsonF.put("agama", jsonS.getString("value_10").replace("1","I"));
                                jsonF.put("status", jsonS.getString("value_11"));
//                                jsonF.put("kerja", jsonS.getString("value_12"));
//                                jsonF.put("negara", jsonS.getString("value_13"));
                                if(!jsonS.getString("value_13").contains("WN")){
                                    if(!jsonS.getString("value_14").contains("WN")){
                                        jsonF.put("kerja", jsonS.getString("value_14"));
                                        jsonF.put("negara", jsonS.getString("value_15"));
                                    }else{
                                        jsonF.put("kerja", jsonS.getString("value_13"));
                                        jsonF.put("negara", jsonS.getString("value_14"));
                                    }
                                }else{
                                    jsonF.put("kerja", jsonS.getString("value_12"));
                                    jsonF.put("negara", jsonS.getString("value_13"));
                                }
                            }else {
//                            for (int z=7; z< jsonS.length(); z++){
//
//                            }
                                jsonF.put("alamat", jsonS.getString("value_6")+" "+jsonS.getString("value_7"));
                                jsonF.put("rtrw", jsonS.getString("value_8"));
                                jsonF.put("keldes", jsonS.getString("value_9"));
                                jsonF.put("kec", jsonS.getString("value_10"));
                                jsonF.put("agama", jsonS.getString("value_11").replace("1","I"));
                                jsonF.put("status", jsonS.getString("value_12"));
//                                jsonF.put("kerja", jsonS.getString("value_13"));
//                                jsonF.put("negara", jsonS.getString("value_14"));

                                if(!jsonS.getString("value_14").contains("WN")){
                                    if(!jsonS.getString("value_15").contains("WN")){
                                        jsonF.put("kerja", jsonS.getString("value_15"));
                                        jsonF.put("negara", jsonS.getString("value_16"));
                                    }else{
                                        jsonF.put("kerja", jsonS.getString("value_14"));
                                        jsonF.put("negara", jsonS.getString("value_15"));
                                    }
                                }else{
                                    jsonF.put("kerja", jsonS.getString("value_13"));
                                    jsonF.put("negara", jsonS.getString("value_14"));
                                }
                            }
                        }


                        json.put("filter", jsonF);
//                        json.put("source", jsonS);
                        json.put("type", "KTP");

                        json.put("message", "Scan succeed");
                    } else if(arrayStr[2].substring(0,4).equals("NPWP")){
                        jsonF.put("npwp", arrayStr[2].substring(5));
                        jsonF.put("nama", arrayStr[3].toUpperCase());
                        jsonF.put("nik", arrayStr[4].replace("NIK:",""));
                        String alamat="";
                        for (int a=5; a< arrayStr.length-1; a++){
                            alamat = alamat+", "+arrayStr[a].toUpperCase();
                        }
                        jsonF.put("alamat", alamat.substring(1));
                        jsonF.put("kpp", arrayStr[arrayStr.length-1].toUpperCase());

                        json.put("filter", jsonF);
//                        json.put("source", jsonS);
                        json.put("type", "NPWP");

                        json.put("message", "Scan succeed");
                    } else{
//                        json.put("filter", jsonF);
//                        json.put("source", jsonS);
                        json.put("type", "Not Found");
                        json.put("message", "Try Again, Type Not Found");
                    }

                }
            } else {
                json.put("message","Could not set up the detector!");
            }
            return json;
        } catch (Exception e) {
            try {
                json.put("message","Failed");
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            return json;
        }

    }

}
