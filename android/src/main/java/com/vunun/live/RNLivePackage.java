package com.vunun.live;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.vunun.librestreaming.RNLrsModule;
import com.vunun.librestreaming.RNLrsViewManager;
import com.vunun.yasea.RNYaseaModule;
import com.vunun.yasea.RNYaseaViewManager;

public class RNLivePackage implements ReactPackage {

  @Override
  public List<NativeModule> createNativeModules (ReactApplicationContext context) {
      List<NativeModule> modules = new ArrayList<>();
      modules.add(new RNLrsModule(context));
      modules.add(new RNYaseaModule(context));
      return modules;
  }
  @Override
  public List<Class<? extends JavaScriptModule>> createJSModules() {
      return Collections.emptyList();
  }
  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext context) {
      List<ViewManager> managers = new ArrayList<>();
      managers.add(new RNLrsViewManager());
      managers.add(new RNYaseaViewManager());
      return managers;
  }
}

