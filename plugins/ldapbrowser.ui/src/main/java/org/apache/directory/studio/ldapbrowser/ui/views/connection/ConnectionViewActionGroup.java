/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 */

package org.apache.directory.studio.ldapbrowser.ui.views.connection;

import org.apache.directory.studio.connection.ui.actions.ConnectionViewActionProxy;
import org.apache.directory.studio.connection.ui.widgets.ConnectionActionGroup;
import org.apache.directory.studio.ldapbrowser.ui.actions.ExportConnectionsAction;
import org.apache.directory.studio.ldapbrowser.ui.actions.ImportConnectionsAction;
import org.apache.directory.studio.ldapbrowser.ui.actions.ImportExportAction;
import org.apache.directory.studio.ldapbrowser.ui.actions.OpenSchemaBrowserAction;
import org.apache.directory.studio.ldapbrowser.ui.actions.PasswordModifyExtendedOperationAction;
import org.apache.directory.studio.ldapbrowser.ui.actions.ReloadSchemaAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;

// nieuw toegevoegd:
import org.apache.directory.studio.ldapbrowser.ui.dialogs.ConnectionSelectorDialog;
import org.apache.directory.studio.ldapbrowser.core.model.IBrowserConnection;

/**
 * This class manages all the actions of the connection view.
 * 
 * @author Apache Directory Project
 */
public class ConnectionViewActionGroup extends ConnectionActionGroup {

    /** The connection view */
    private ConnectionView view;

    /** The link with editor action. */
    private LinkWithEditorAction linkWithEditorAction;

    /** Constants for actions */
    private static final String importDsmlAction = "importDsmlAction"; //$NON-NLS-1$
    private static final String exportDsmlAction = "exportDsmlAction"; //$NON-NLS-1$
    private static final String importLdifAction = "importLdifAction"; //$NON-NLS-1$
    private static final String exportLdifAction = "exportLdifAction"; //$NON-NLS-1$
    private static final String exportCsvAction = "exportCsvAction"; //$NON-NLS-1$
    private static final String exportExcelAction = "exportExcelAction"; //$NON-NLS-1$
    private static final String exportOdfAction = "exportOdfAction"; //$NON-NLS-1$
    private static final String importConnectionsAction = "importConnectionsAction"; //$NON-NLS-1$
    private static final String exportConnectionsAction = "exportConnectionsAction"; //$NON-NLS-1$
    private static final String openSchemaBrowserAction = "openSchemaBrowserAction"; //$NON-NLS-1$
    private static final String reloadSchemaAction = "reloadSchemaAction"; //$NON-NLS-1$
    private static final String passwordModifyExtendedOperationAction = "passwordModifyExtendedOperation"; //$NON-NLS-1$

    /**
     * Creates a new instance of ConnectionViewActionGroup and creates all actions.
     *
     * @param view the connection view
     */
    public ConnectionViewActionGroup(ConnectionView view) {
        super(view.getMainWidget(), view.getConfiguration());
        this.view = view;
        TreeViewer viewer = view.getMainWidget().getViewer();

        linkWithEditorAction = new LinkWithEditorAction(view);
        connectionActionMap.put(importDsmlAction, new ConnectionViewActionProxy(viewer, this,
                new ImportExportAction(ImportExportAction.TYPE_IMPORT_DSML)));
        connectionActionMap.put(exportDsmlAction, new ConnectionViewActionProxy(viewer, this,
                new ImportExportAction(ImportExportAction.TYPE_EXPORT_DSML)));
        connectionActionMap.put(importLdifAction, new ConnectionViewActionProxy(viewer, this,
                new ImportExportAction(ImportExportAction.TYPE_IMPORT_LDIF)));
        connectionActionMap.put(exportLdifAction, new ConnectionViewActionProxy(viewer, this,
                new ImportExportAction(ImportExportAction.TYPE_EXPORT_LDIF)));
        connectionActionMap.put(exportCsvAction, new ConnectionViewActionProxy(viewer, this,
                new ImportExportAction(ImportExportAction.TYPE_EXPORT_CSV)));
        connectionActionMap.put(exportExcelAction, new ConnectionViewActionProxy(viewer, this,
                new ImportExportAction(ImportExportAction.TYPE_EXPORT_EXCEL)));
        connectionActionMap.put(exportOdfAction, new ConnectionViewActionProxy(viewer, this,
                new ImportExportAction(ImportExportAction.TYPE_EXPORT_ODF)));
        connectionActionMap.put(importConnectionsAction,
                new ConnectionViewActionProxy(viewer, this, new ImportConnectionsAction()));
        connectionActionMap.put(exportConnectionsAction,
                new ConnectionViewActionProxy(viewer, this, new ExportConnectionsAction()));
        connectionActionMap.put(openSchemaBrowserAction,
                new ConnectionViewActionProxy(viewer, this, new OpenSchemaBrowserAction()));
        connectionActionMap.put(reloadSchemaAction,
                new ConnectionViewActionProxy(viewer, this, new ReloadSchemaAction()));
        connectionActionMap.put(passwordModifyExtendedOperationAction,
                new ConnectionViewActionProxy(viewer, this, new PasswordModifyExtendedOperationAction()));
    }

    /**
     * {@inheritDoc}
     */
    public void dispose() {
        if (view != null) {
            linkWithEditorAction.dispose();
            linkWithEditorAction = null;
            view = null;
        }
        super.dispose();
    }

    /**
     * {@inheritDoc}
     */
    public void menuAboutToShow(IMenuManager menuManager) {

        // add
        menuManager.add((IAction) connectionActionMap.get(NEW_CONNECTION_ACTION));
        menuManager.add((IAction) connectionActionMap.get(NEW_CONNECTION_FOLDER_ACTION));
        menuManager.add(new Separator());

        // open/close
        if (((IAction) connectionActionMap.get(CLOSE_CONNECTION_ACTION)).isEnabled()) {
            menuManager.add((IAction) connectionActionMap.get(CLOSE_CONNECTION_ACTION));
        } else if (((IAction) connectionActionMap.get(OPEN_CONNECTION_ACTION)).isEnabled()) {
            IAction openConnectionAction = connectionActionMap.get(OPEN_CONNECTION_ACTION);
            if (openConnectionAction != null) {
                openConnectionAction.setText("Open Connection\tCtrl+O");
                openConnectionAction.setAccelerator(SWT.CTRL | 'O');
                menuManager.add(openConnectionAction);
            }
        }
        menuManager.add(new Separator());

        menuManager.add((IAction) connectionActionMap.get(openSchemaBrowserAction));
        menuManager.add((IAction) connectionActionMap.get(reloadSchemaAction));
        menuManager.add(new Separator());

        // copy/paste/...
        menuManager.add((IAction) connectionActionMap.get(COPY_CONNECTION_ACTION));
        menuManager.add((IAction) connectionActionMap.get(PASTE_CONNECTION_ACTION));
        menuManager.add((IAction) connectionActionMap.get(DELETE_CONNECTION_ACTION));
        menuManager.add((IAction) connectionActionMap.get(RENAME_CONNECTION_ACTION));
        menuManager.add(new Separator());

        // Test submenu
        MenuManager testMenuManager = new MenuManager("Dit is een test menu");
        testMenuManager.add((IAction) connectionActionMap.get(importLdifAction));
        testMenuManager.add((IAction) connectionActionMap.get(PROPERTY_DIALOG_ACTION));
        menuManager.add(testMenuManager);
        menuManager.add(new Separator());

        // import/export
        MenuManager importMenuManager = new MenuManager(Messages.getString("ConnectionViewActionGroup.Import")); //$NON-NLS-1$
        importMenuManager.add((IAction) connectionActionMap.get(importLdifAction));
        importMenuManager.add((IAction) connectionActionMap.get(importDsmlAction));
        importMenuManager.add(new Separator());
        importMenuManager.add((IAction) connectionActionMap.get(importConnectionsAction));
        importMenuManager.add(new Separator());
        menuManager.add(importMenuManager);

        MenuManager exportMenuManager = new MenuManager(Messages.getString("ConnectionViewActionGroup.Export")); //$NON-NLS-1$
        exportMenuManager.add((IAction) connectionActionMap.get(exportLdifAction));
        exportMenuManager.add((IAction) connectionActionMap.get(exportDsmlAction));
        exportMenuManager.add(new Separator());
        exportMenuManager.add((IAction) connectionActionMap.get(exportCsvAction));
        exportMenuManager.add((IAction) connectionActionMap.get(exportExcelAction));
        exportMenuManager.add((IAction) connectionActionMap.get(exportOdfAction));
        exportMenuManager.add(new Separator());
        exportMenuManager.add((IAction) connectionActionMap.get(exportConnectionsAction));
        exportMenuManager.add(new Separator());
        menuManager.add(exportMenuManager);
        menuManager.add(new Separator());

     // 🔹 Nieuw menu-item om de ConnectionSelectorDialog te openen
        Action showAllConnectionsAction = new Action("Show All Connections...") {
            @Override
            public void run() {
                Shell shell = Display.getDefault().getActiveShell();
                ConnectionSelectorDialog dialog = new ConnectionSelectorDialog(shell);

                if (dialog.open() == Window.OK) {
                    IBrowserConnection selected = dialog.getSelectedConnection();
                    if (selected == null || selected.getConnection() == null) {
                        MessageDialog.openInformation(shell, "No Selection", "No connection selected.");
                        return;
                    }

                    // 1️⃣ De bijbehorende Connection ophalen (dit is de key waarop de actie werkt)
                    org.apache.directory.studio.connection.core.Connection connection = selected.getConnection();

                    // 2️⃣ De selectie in de viewer zetten
                    TreeViewer tv = view.getMainWidget().getViewer();
                    tv.setSelection(new StructuredSelection(connection), true);
                    tv.refresh();

                    // 3️⃣ De ingebouwde actie ophalen en uitvoeren
                    IAction openConnectionAction = connectionActionMap.get(OPEN_CONNECTION_ACTION);

                    if (openConnectionAction != null && openConnectionAction.isEnabled()) {
                        // ✅ Dit triggert de volledige standaard flow (progress + errors)
                        openConnectionAction.run();
                        System.out.println("Connection opened via native action: " + connection.getName());
                    } else {
                        MessageDialog.openError(shell, "Error",
                                "Open Connection action is not available or disabled.");
                    }
                }
            }
        };
        showAllConnectionsAction.setAccelerator(SWT.F6);

        menuManager.add(showAllConnectionsAction);
        menuManager.add(new Separator());


        // additions
        menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

        // extended operations
        MenuManager extendedOperationsMenuManager = new MenuManager(
                Messages.getString("ConnectionViewActionGroup.ExtendedOperations")); //$NON-NLS-1$
        extendedOperationsMenuManager.add(connectionActionMap.get(passwordModifyExtendedOperationAction));
        menuManager.add(extendedOperationsMenuManager);
        menuManager.add(new Separator());

        // properties
        menuManager.add((IAction) connectionActionMap.get(PROPERTY_DIALOG_ACTION));
    }
    
    public IAction getOpenConnectionAction() {
        return connectionActionMap.get(OPEN_CONNECTION_ACTION);
    }
}
