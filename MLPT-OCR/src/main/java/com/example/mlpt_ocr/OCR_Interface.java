package com.example.mlpt_ocr;

import android.content.Intent;
import android.os.Bundle;

public interface OCR_Interface {
    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onSaveInstanceState(Bundle outState);
}
