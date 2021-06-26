package com.example.mlpt_ocr;

import android.app.Activity;
import android.content.Intent;
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

public class OCR {
    public static Uri imageUri;

    public static void takePhoto(Activity x, String app){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(x,
                app + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        x.startActivityForResult(intent, 10);
    }

    public static JSONObject convertText(Activity c, ImageView iv) {
        JSONObject json = new JSONObject();
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(imageUri);
            c.sendBroadcast(mediaScanIntent);
            String hasil = null;
            int targetW = 600;
            int targetH = 600;
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
                    JSONObject jsonS = new JSONObject();
                    for (int a=0; a< arrayStr.length; a++){
                        System.out.println("arrayStr "+a+". "+arrayStr[a]);
                        jsonS.put("value_"+a, arrayStr[a]);
                    }

                    if(arrayStr[2].substring(0,3).equals("NIK")){
                        json.put("nik", arrayStr[3].replace(":",""));
                        json.put("nama", arrayStr[4].toUpperCase());
                        json.put("lahir", arrayStr[5].toUpperCase());
                        json.put("jk", arrayStr[8].substring(0,9));
                        json.put("source", jsonS);

                        json.put("message", "Scan succeed");
                    } else if(arrayStr[2].substring(0,4).equals("NPWP")){
                        json.put("npwp", arrayStr[2].substring(5));
                        json.put("nama", arrayStr[3].toUpperCase());
                        json.put("nik", arrayStr[4].replace("NIK:",""));
                        json.put("alamat", arrayStr[5]+" "+arrayStr[6]);
                        json.put("source", jsonS);

                        json.put("message", "Scan succeed");
                    } else{

                        json.put("source", jsonS);
                        json.put("message", "Scan succeed, Type Not Found");
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
