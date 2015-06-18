// Copyright (c) 2014-2-19 wanghb@dearcoin.com. All rights reserved.
package com.yuantiku.knife;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.encode.QRCodeEncoder;

/**
 * @author wanghb
 */
public class QrCodeHelper {
  public static final int SCAN_QR_CODE = 2134;

  public static void scanQrCode(Activity act) {
    act.startActivityForResult(getScanIntent(act), SCAN_QR_CODE);
  }


  public static String getScanResult(Intent data) {
    return data == null ? null : data.getStringExtra(Intents.Scan.RESULT);
  }

  private static Intent getScanIntent(Context context) {
    Intent scan = new Intent(context, CaptureActivity.class);
    scan.setAction(Intents.Scan.ACTION);
    scan.putExtra(Intents.Scan.FORMATS, "QR_CODE");
    scan.putExtra(Intents.Scan.PROMPT_MESSAGE, "Scan qrcode");
    return scan;
  }

  @SuppressWarnings("deprecation")
  @SuppressLint("NewApi")
  public static Bitmap encode(Context context, String text) {
    WindowManager manager = (WindowManager) context.getSystemService(
        Context.WINDOW_SERVICE);
    int width = 0;
    int height = 0;
    if (Build.VERSION_CODES.HONEYCOMB_MR2 > Build.VERSION.SDK_INT) {
      width = manager.getDefaultDisplay().getWidth();
      height = manager.getDefaultDisplay().getHeight();
    } else {
      Display display = manager.getDefaultDisplay();
      Point displaySize = new Point();
      display.getSize(displaySize);
      width = displaySize.x;
      height = displaySize.y;
    }
    int smallerDimension = width < height ? width : height;

    Intent intent = new Intent(Intents.Encode.ACTION);
    intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
    intent.putExtra(Intents.Encode.DATA, text);
    Bitmap bitmap = null;
    try {
      QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(context, intent,
          smallerDimension, false);
      bitmap = qrCodeEncoder.encodeAsBitmap();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bitmap;
  }
}
