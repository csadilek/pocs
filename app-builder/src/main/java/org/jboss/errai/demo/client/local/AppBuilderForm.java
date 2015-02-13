package org.jboss.errai.demo.client.local;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.demo.client.shared.AppBuilderEndpoint;
import org.jboss.errai.demo.client.shared.AppError;
import org.jboss.errai.demo.client.shared.AppReady;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

@Page(role = DefaultPage.class)
@Templated("AppBuilderForm.html#app-template")
public class AppBuilderForm extends Composite {

  @DataField
  private Element appBuilder = DOM.createDiv();
  
  @DataField
  private Element app1 = DOM.createDiv();
  
  @Inject @DataField
  private Button loadApp;
  
  @Inject
  private Caller<AppBuilderEndpoint> endpoint;
  
  @PostConstruct
  private void onInit() {
    RestClient.setApplicationRoot("/app-builder/api");  
  }
  
  @EventHandler("loadApp")
  private void onSubmit(ClickEvent e) {
    endpoint.call(new ResponseCallback() {
      
      @Override
      public void callback(Response response) {
        if (response.getStatusCode() == Response.SC_OK) {
          showSpinner();
        }
        else {
          Window.alert("Oops something went wrong! Server returned:" + 
        response.getStatusCode());
        }
      }
    }).loadApp("app1");
  }
  
  private void onAppReady(@Observes AppReady appReady) {
    hideSpinner();
    ScriptInjector.fromUrl(appReady.getUrl()).setCallback(new Callback<Void, Exception>() {
      
      @Override
      public void onSuccess(Void result) {
      }
      
      @Override
      public void onFailure(Exception reason) {
        
      }
    })
    .setWindow(ScriptInjector.TOP_WINDOW)
    .inject();
  }
  
  private void onAppError(@Observes AppError appError) {
    hideSpinner();
    Window.alert("Problem loading application");
  }
  
  private native void showSpinner() /*-{
    var target = $doc.getElementById('loading');
    target.style.display = 'block';
    $wnd.spinner.spin(target);
    
    var placeholder = $doc.getElementById('placeholder');
    placeholder.style.display = 'none';
  }-*/;
  
  private native void hideSpinner() /*-{
    var target = $doc.getElementById('loading');
    target.style.display = 'none';
    $wnd.spinner.stop();
  }-*/;
}