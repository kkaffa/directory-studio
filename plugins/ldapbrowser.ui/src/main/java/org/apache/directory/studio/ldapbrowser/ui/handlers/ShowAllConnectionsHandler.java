package org.apache.directory.studio.ldapbrowser.ui.handlers;

import org.apache.directory.studio.connection.core.Connection;
import org.apache.directory.studio.ldapbrowser.core.model.IBrowserConnection;
import org.apache.directory.studio.ldapbrowser.ui.dialogs.ConnectionSelectorDialog;
import org.apache.directory.studio.ldapbrowser.ui.views.connection.ConnectionView;
import org.apache.directory.studio.ldapbrowser.ui.views.connection.ConnectionViewActionGroup;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class ShowAllConnectionsHandler extends AbstractHandler {
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = Display.getDefault().getActiveShell();

        try {
            // 1️⃣ Toon de popup
            ConnectionSelectorDialog dialog = new ConnectionSelectorDialog(shell);
            if (dialog.open() != Window.OK) {
                return null; // gebruiker annuleert
            }

            IBrowserConnection selected = dialog.getSelectedConnection();
            if (selected == null || selected.getConnection() == null) {
                MessageDialog.openInformation(shell, "No Selection", "No connection selected.");
                return null;
            }

            Connection connection = selected.getConnection();

            // 2️⃣ Haal de bestaande LDAP ConnectionView op
            IWorkbenchPage page = PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow()
                    .getActivePage();

            ConnectionView connectionView = (ConnectionView) page.findView(
                    "org.apache.directory.studio.ldapbrowser.ui.views.connection.ConnectionView");

            if (connectionView == null) {
                // Als hij nog niet open is, openen we hem
                page.showView("org.apache.directory.studio.ldapbrowser.ui.views.connection.ConnectionView");
                connectionView = (ConnectionView) page.findView(
                        "org.apache.directory.studio.ldapbrowser.ui.views.connection.ConnectionView");
            }

            if (connectionView == null) {
                MessageDialog.openError(shell, "Error", "Could not locate Connection View.");
                return null;
            }

            // 3️⃣ Selecteer de verbinding in de viewer
            TreeViewer tv = connectionView.getMainWidget().getViewer();
            tv.setSelection(new StructuredSelection(connection), true);
            tv.refresh();

            // 4️⃣ Gebruik de bestaande actie
            ConnectionViewActionGroup actionGroup = connectionView.getActionGroup();
            IAction openAction = actionGroup.getOpenConnectionAction();
            if (openAction != null && openAction.isEnabled()) {
                openAction.run(); // ✅ triggert progress UI
            } else {
                MessageDialog.openError(shell, "Error",
                        "Open Connection action not available or disabled.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.openError(shell, "Error",
                    "Failed to open connection:\n" + e.getMessage());
        }

        return null;
    }
}
