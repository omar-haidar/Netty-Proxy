package dev.omar.nettyproxy.viewmodel;

import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

public class GlobalViewModelStore implements ViewModelStoreOwner {
    
    private static final ViewModelStore store = new ViewModelStore();
    
    private static final GlobalViewModelStore INSTANCE = new GlobalViewModelStore();
    
    public static GlobalViewModelStore getInstance() {
    	return INSTANCE;
    }
    @Override
    public ViewModelStore getViewModelStore() {
        return store;
    }
}
