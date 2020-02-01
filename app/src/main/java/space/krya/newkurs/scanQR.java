package space.krya.newkurs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView ScannreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr);

        ScannreView = new ZXingScannerView(this);
        setContentView(ScannreView);
    }

    @Override
    public void handleResult(Result result) {
        scan.textViewResult.setText(result.getText());
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        ScannreView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ScannreView.setResultHandler(this);
        ScannreView.startCamera();
    }
}
