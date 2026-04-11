package dev.omar.nettyproxy.ui.base;

import android.content.Intent;

import android.provider.Settings;
import android.net.Uri;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import dev.omar.nettyproxy.utils.Utils;

public abstract class BaseActivity extends AppCompatActivity {
    
    final ActivityResultLauncher<Intent> requestIBOLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
        
    });
    
    protected void requestIgnoreBatteryOptimizationIfNeed() {
        if (!Utils.isIgnoringBatteryOptimizations(this)) {
            requestIBOLauncher.launch(createRequestIgnoreBatteryOptimizationIntent());
        }
    }

    private Intent createRequestIgnoreBatteryOptimizationIntent() {
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        return intent;
    }
}
