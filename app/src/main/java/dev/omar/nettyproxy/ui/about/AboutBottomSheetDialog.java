package dev.omar.nettyproxy.ui.about;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dev.omar.nettyproxy.R;
import dev.omar.nettyproxy.databinding.LayoutAboutBinding;

public class AboutBottomSheetDialog extends BottomSheetDialogFragment {
    
    private LayoutAboutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        binding = LayoutAboutBinding.inflate(inflater, parent, false);
        return binding.getRoot();
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        
        binding.txtAboutMessage.setMovementMethod(LinkMovementMethod.getInstance()); // لجعل الروابط قابلة للنقر

        String aboutText = getString(R.string.about_description);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            binding.txtAboutMessage.setText(Html.fromHtml(aboutText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            binding.txtAboutMessage.setText(Html.fromHtml(aboutText));
        }
        binding.btnAboutOk.setOnClickListener(v->dismiss());
        
        
        
    }
}
