package dev.omar.nettyproxy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.core.content.ContextCompat;

import dev.omar.nettyproxy.databinding.ActivityMainBinding;
import dev.omar.nettyproxy.proxy.ProxyController;
import dev.omar.nettyproxy.ui.about.AboutBottomSheetDialog;
import dev.omar.nettyproxy.ui.base.BaseActivity;
import dev.omar.nettyproxy.utils.Utils;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarContainer.toolbar);
        if (Utils.hasNotificationPermissions(this)) {
            initilizeLogic();
        } else {
            Utils.showGrantNotificationPermissionDialog(this);
        }
    }
    
    private void initilizeLogic() {
        requestIgnoreBatteryOptimizationIfNeed();
        ProxyController.getInstance()
                .isRunningProxyService()
                .observe(
                        this,
                        isRunning -> {
                            updateUI();
                        });

        ProxyController.getInstance()
                .getServerProvider()
                .observe(
                        this,
                        provider -> {
                            if (provider == null) {
                                binding.txtProxyIp.setText("...");
                                binding.txtProxyPort.setText("...");
                            } else {
                                binding.txtProxyIp.setText(provider.provideServer().getIP());
                                binding.txtProxyPort.setText(
                                        "" + provider.provideServer().getPort());
                            }
                        });
        binding.fab.setOnClickListener(v -> toggleProxyServiceRunning());
        binding.switchAutoStart.setChecked(App.get().getSettings().isStartupWithBoot());
        binding.switchAutoStart.setOnCheckedChangeListener(
                (cb, isChecked) -> App.get().getSettings().setStartWithBoot(isChecked));
    }

    private void updateUI() {
        boolean isRunningService = ProxyController.getInstance().isRunningProxyService().getValue();
        binding.txtServiceStatus.setText(isRunningService ? "STARTED" : "STOPPED");
        binding.fab.setImageResource(isRunningService ? R.drawable.ic_stop : R.drawable.ic_run);
        binding.txtServiceStatus.setTextColor(Utils.getColorByStatus(this, isRunningService));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_info:
                new AboutBottomSheetDialog().show(getSupportFragmentManager(), "AboutUs");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleProxyServiceRunning() {
        requestIgnoreBatteryOptimizationIfNeed();
        ProxyController.getInstance().toggleProxyServiceRunning(this);
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
