package dev.omar.nettyproxy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import dev.omar.nettyproxy.databinding.ActivityMainBinding;
import dev.omar.nettyproxy.proxy.ProxyController;
import dev.omar.nettyproxy.utils.Utils;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_IGNORE_BATTERY_OPTIMIZATIONS = 1002;
    private ActivityMainBinding binding;
    private PowerManager mPowerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarContainer.toolbar);
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_DENIED) {
                Utils.showGrantNotificationPermissionDialog(this);
            } else {
                initilizeLogic();
            }

        } else {
            initilizeLogic();
        }
    }

    private void initilizeLogic() {
        ProxyController.getInstance()
                .isRunningProxyService()
                .observe(
                        this,
                        isRunning -> {
                            updateUI();
                        });
        binding.fab.setOnClickListener(v -> toggleProxyServiceRunning());
    }

    public void updateUI() {
        boolean isRunningService = ProxyController.getInstance().isRunningProxyService().getValue();
        binding.txtProxyIp.setText(Utils.getLocalIpAddress());
        binding.txtProxyPort.setText("8080");
        binding.txtServiceStatus.setText(isRunningService ? "STARTED" : "STOPPED");
        binding.fab.setImageResource(isRunningService ? R.drawable.ic_stop : R.drawable.ic_run);
        binding.txtServiceStatus.setTextColor(
                isRunningService
                        ? ContextCompat.getColor(this, R.color.success)
                        : ContextCompat.getColor(this, R.color.error));
        invalidateMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (menu.findItem(R.id.menu_item_run) != null) {
            MenuItem item = menu.findItem(R.id.menu_item_run);
            item.setIcon(
                    ProxyController.getInstance().isRunningProxyService().getValue()
                            ? R.drawable.ic_stop_circle
                            : R.drawable.ic_run_outline);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_run:
                toggleProxyServiceRunning();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleProxyServiceRunning() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mPowerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                return;
            } else {
                ProxyController.getInstance().toggleProxyServiceRunning(this);
            }
        } else {
            ProxyController.getInstance().toggleProxyServiceRunning(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Utils.REQ_POST_NOTIFICATION) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                initilizeLogic();
            } else {
                Utils.showGrantNotificationPermissionDialog(this);
            }
        }
    }
}
