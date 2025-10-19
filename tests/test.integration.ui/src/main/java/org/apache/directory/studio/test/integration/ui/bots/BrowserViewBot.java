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
 *  
 */
package org.apache.directory.studio.test.integration.ui.bots;


import org.apache.directory.studio.ldapbrowser.core.BrowserCoreMessages;
import org.apache.directory.studio.test.integration.ui.utils.ContextMenuHelper;
import org.apache.directory.studio.test.integration.ui.utils.JobWatcher;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;


public class BrowserViewBot
{
    private SWTWorkbenchBot bot;
    private BrowserWidgetBot browserBot;

    public BrowserViewBot()
    {
        bot = new SWTWorkbenchBot();
        SWTBotView view = bot.viewByTitle( "LDAP Browser" );
        view.show();

        browserBot = new BrowserWidgetBot( view.bot() );
    }


    public String getSelectedEntry()
    {
        return browserBot.getSelectedEntry();
    }


    public boolean existsEntry( String... path )
    {
        return browserBot.existsEntry( path );
    }


    public void selectEntry( String... path )
    {
        boolean wait = !path[path.length - 1].startsWith( "Quick Search" )
            && !path[path.length - 1].equals( "No Results" )
            && !"Searches".equals( path[0] );
        browserBot.selectEntry( wait, path );
    }


    public void selectChildrenOfEntry( String[] children, String... path )
    {
        browserBot.selectChildrenOfEntry( children, path );
    }


    public ReferralDialogBot selectEntryExpectingReferralDialog( String... path )
    {
        return browserBot.selectEntryExpectingReferralDialog( path );
    }


    public void selectAndExpandEntry( String... path )
    {
        selectEntry( path );
        expandEntry( path );
    }


    public void expandEntry( String... path )
    {
        browserBot.expandEntry( path );
    }


    public void waitForEntry( String... path )
    {
        browserBot.waitForEntry( path );
    }


    public ReferralDialogBot expandEntryExpectingReferralDialog( String... path )
    {
        return browserBot.expandEntryExpectingReferralDialog( path );
    }


    public NewEntryWizardBot openNewEntryWizard()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "New", "New Entry..." );
        return new NewEntryWizardBot();
    }


    public SearchDialogBot openSearchDialog()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "New", "New Search..." );
        return new SearchDialogBot();
    }


    public RenameEntryDialogBot openRenameDialog()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Rename Entry..." );
        return new RenameEntryDialogBot();
    }


    public MoveEntriesDialogBot openMoveEntryDialog()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Move Entry..." );
        return new MoveEntriesDialogBot();
    }


    public DeleteDialogBot openDeleteDialog()
    {
        if ( browserBot.getTree().selectionCount() == 1 )
        {
            ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Delete Entry" );
            return new DeleteDialogBot( DeleteDialogBot.DELETE_ENTRY_TITLE );
        }
        else
        {
            ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Delete Entries" );
            return new DeleteDialogBot( DeleteDialogBot.DELETE_ENTRIES_TITLE );
        }
    }


    public ExportWizardBot openExportLdifWizard()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Export", "LDIF Export..." );
        return new ExportWizardBot( ExportWizardBot.EXPORT_LDIF_TITLE );
    }


    public ExportWizardBot openExportDsmlWizard()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Export", "DSML Export..." );
        return new ExportWizardBot( ExportWizardBot.EXPORT_DSML_TITLE );
    }


    public ExportWizardBot openExportCsvWizard()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Export", "CSV Export..." );
        return new ExportWizardBot( ExportWizardBot.EXPORT_CSV_TITLE );
    }


    public ImportWizardBot openImportLdifWizard()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Import", "LDIF Import..." );
        return new ImportWizardBot( ImportWizardBot.IMPORT_LDIF_TITLE );
    }


    public ImportWizardBot openImportDsmlWizard()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Import", "DSML Import..." );
        return new ImportWizardBot( ImportWizardBot.IMPORT_DSML_TITLE );
    }


    public void refresh()
    {
        JobWatcher watcher = new JobWatcher( BrowserCoreMessages.jobs__init_entries_title_subonly );
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Reload Entry" );
        watcher.waitUntilDone();
    }


    public void copy()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Copy" );
    }


    public void paste()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Paste" );
    }


    public void pasteEntry()
    {
        pasteEntries( 1 );
    }


    public void pasteEntries( int numEntries )
    {
        if ( numEntries > 1 )
        {
            JobWatcher watcher = new JobWatcher( BrowserCoreMessages.jobs__copy_entries_name_n );
            ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Paste" );
            watcher.waitUntilDone();
        }
        else
        {
            JobWatcher watcher = new JobWatcher( BrowserCoreMessages.jobs__copy_entries_name_1 );
            ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Paste" );
            watcher.waitUntilDone();
        }
    }


    public SelectCopyDepthDialogBot pasteEntriesExpectingSelectCopyDepthDialog( int numEntries )
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Paste" );
        return new SelectCopyDepthDialogBot( numEntries );
    }


    public SelectCopyStrategyBot pasteEntriesExpectingSelectCopyStrategy()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Paste" );
        return new SelectCopyStrategyBot();
    }


    public SearchPropertiesDialogBot pasteSearch( String searchName )
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "Paste" );
        return new SearchPropertiesDialogBot( searchName );
    }


    public PasswordModifyExtendedOperationDialogBot openPasswordModifyExtendedOperationDialog()
    {
        ContextMenuHelper.clickContextMenu( browserBot.getTree(), "", "Password Modify..." );
        return new PasswordModifyExtendedOperationDialogBot();
    }


    public void typeQuickSearchAttributeType( String attributeType )
    {
        bot.comboBox( 0 ).setText( attributeType );
    }


    public void typeQuickSearchValue( String value )
    {
        bot.comboBox( 2 ).setText( value );
    }


    public void clickRunQuickSearchButton()
    {
        bot.buttonWithTooltip( "Run Quick Search" ).click();
    }


    public boolean isQuickSearchEnabled()
    {
        return bot.comboBox( 0 ).isEnabled();
    }


    public void waitUntilEntryIsSelected( String label )
    {
        browserBot.waitUntilEntryIsSelected( label );
    }

}
