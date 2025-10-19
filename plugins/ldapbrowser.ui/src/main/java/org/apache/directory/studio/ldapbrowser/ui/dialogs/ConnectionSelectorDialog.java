package org.apache.directory.studio.ldapbrowser.ui.dialogs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.directory.studio.ldapbrowser.core.BrowserCorePlugin;
import org.apache.directory.studio.ldapbrowser.core.model.IBrowserConnection;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog to show all LDAP connections with a search bar and working selection.
 */
public class ConnectionSelectorDialog extends Dialog {

    private Text searchText;
    private TableViewer viewer;
    private List<IBrowserConnection> allConnections;
    private IBrowserConnection selectedConnection;

    public ConnectionSelectorDialog(Shell parentShell) {
        super(parentShell);

        // ✅ Haal de actieve verbindingen op
        IBrowserConnection[] conns = BrowserCorePlugin.getDefault()
                .getConnectionManager()
                .getBrowserConnections();
        allConnections = Arrays.asList(conns);
        System.out.println("Loaded connections: " + allConnections.size());
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));

        // Zoekveld
        searchText = new Text(container, SWT.SEARCH | SWT.ICON_SEARCH | SWT.CANCEL);
        searchText.setMessage("Search connections...");
        searchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Tabel
        viewer = new TableViewer(container, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL);
        viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        viewer.setContentProvider(ArrayContentProvider.getInstance());
        viewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                IBrowserConnection bc = (IBrowserConnection) element;
                return bc.getConnection().getName();
            }
        });
        viewer.setInput(allConnections);

        // ✅ Listener die geselecteerd item opslaat
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection sel = (IStructuredSelection) event.getSelection();
                selectedConnection = (IBrowserConnection) sel.getFirstElement();
                if (selectedConnection != null) {
                    System.out.println("Selected in table: " + selectedConnection.getConnection().getName());
                }
            }
        });

        // ✅ Dubbelklik = zelfde als OK
        viewer.getTable().addListener(SWT.DefaultSelection, e -> okPressed());

        // ✅ Zoekfilter
        searchText.addModifyListener(e -> {
            String filter = searchText.getText().toLowerCase();
            List<IBrowserConnection> filtered = allConnections.stream()
                    .filter(c -> c.getConnection().getName().toLowerCase().contains(filter))
                    .collect(Collectors.toList());
            viewer.setInput(filtered);
        });

        return container;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "Open", true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /** ✅ Retourneer de geselecteerde verbinding aan de aanroepende klasse. */
    public IBrowserConnection getSelectedConnection() {
        return selectedConnection;
    }
}
