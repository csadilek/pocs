package org.jboss.errai.demo.client.local;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.demo.client.AppScopedService;
import org.jboss.errai.demo.client.DepScopedService;
import org.jboss.errai.demo.client.Employee;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;

@Templated
@Page(role=DefaultPage.class)
public class Consumer extends Composite {
  
  @Inject @DataField private Anchor a1;
  @Inject @DataField private Anchor a2;
  @Inject @DataField private Anchor a3;

  // Injecting an instances of types provided by a different script at runtime
  // (not known/inherited in this GWT module) (see dyn-modules/module-producer)
  
  // Provided by a GWT-compiled script
  @Inject
  private AppScopedService appScopedService;

  // Provided by a native script
  @Inject
  private DepScopedService depScopedService;

  @EventHandler("a1")
  private void onA1(final ClickEvent e) {
    Window.alert(appScopedService.hello());
  }
  
  @EventHandler("a2")
  private void onA2(final ClickEvent e) {
    Window.alert(depScopedService.hello());
  }
  
  @EventHandler("a3")
  private void onA3(final ClickEvent e) {
    final Employee emp = JsTypeFactory.create("Christian", "Sadilek", "csadilek@redhat.com");
    Window.alert(emp.getDetailString());
  }
}