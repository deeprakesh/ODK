/*
 * Copyright (C) 2011 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.aggregate.client;

import java.util.HashMap;
import java.util.Map;

import org.opendatakit.aggregate.client.permissions.PermissionsSheet;
import org.opendatakit.aggregate.constants.common.SubTabs;
import org.opendatakit.aggregate.constants.common.Tabs;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TabPanel;

public class ManageTabUI extends TabPanel {

  // Management Navigation
  public static final SubTabs[] MANAGEMENT_MENU = { SubTabs.FORMS,
      SubTabs.PUBLISH, SubTabs.PERMISSIONS, SubTabs.PREFERENCES};

  // Sub tabs
  private FormsSubTab formsSubTab;
  private PublishSubTab publishSubTab;
  private PreferencesSubTab preferencesSubTab;
  private PermissionsSheet permissionsSheet;

  private Map<SubTabs, SubTabInterface> subTabMap;
  
  public ManageTabUI(AggregateUI baseUI) {
    super();

    subTabMap = new HashMap<SubTabs,SubTabInterface>();
    
    // build the UI
    formsSubTab = new FormsSubTab(baseUI);
    this.add(formsSubTab, SubTabs.FORMS.getTabLabel());
    subTabMap.put(SubTabs.FORMS, formsSubTab);

    publishSubTab = new PublishSubTab(baseUI);
    this.add(publishSubTab, SubTabs.PUBLISH.getTabLabel());
    subTabMap.put(SubTabs.PUBLISH, publishSubTab);
    
    permissionsSheet = new PermissionsSheet(this);
    this.add(permissionsSheet, SubTabs.PERMISSIONS.getTabLabel());

    preferencesSubTab = new PreferencesSubTab(baseUI);
    this.add(preferencesSubTab, SubTabs.PREFERENCES.getTabLabel());
    subTabMap.put(SubTabs.PREFERENCES, preferencesSubTab);

    
    getElement().setId("second_level_menu");
    
    // navigate to proper subtab on creation based on the URL
    UrlHash hash = UrlHash.getHash();
    int selected = 0;
    String subMenu = hash.get(UrlHash.SUB_MENU);
    for (int i = 0; i < MANAGEMENT_MENU.length; i++) {
      if (subMenu.equals(MANAGEMENT_MENU[i].getTabLabel())) {
        selected = i;
      }
    }
    this.selectTab(selected);
    baseUI.getTimer().setCurrentSubTab(MANAGEMENT_MENU[selected]);

    // creating the sub tab click handlers
    for (int i = 0; i < MANAGEMENT_MENU.length; i++) {
      ClickHandler handler = baseUI.getSubMenuClickHandler(Tabs.MANAGEMENT, MANAGEMENT_MENU[i]);
      this.getTabBar().getTab(i).addClickHandler(handler);
    }
  }

  // TODO get rid of this function
  public void setSubSelection(String subMenu, String subSubMenu) {
    UrlHash hash = UrlHash.getHash();
    hash.clear();
    hash.set(UrlHash.MAIN_MENU, Tabs.MANAGEMENT.getTabLabel());
    hash.set(UrlHash.SUB_MENU, subMenu);
    hash.set(UrlHash.FORM, subSubMenu);
    hash.put();
  }

  public SubTabInterface getSubTab(SubTabs subTab) {
    return subTabMap.get(subTab);
  }
}