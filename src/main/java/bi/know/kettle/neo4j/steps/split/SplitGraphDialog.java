package bi.know.kettle.neo4j.steps.split;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class SplitGraphDialog extends BaseStepDialog implements StepDialogInterface {

  private static Class<?> PKG = SplitGraphMeta.class; // for i18n purposes, needed by Translator2!!

  private Text wStepname;

  private CCombo wGraphField;
  private TextVar wTypeField;
  private TextVar wIdField;
  private TextVar wPropertySetField;

  private SplitGraphMeta input;

  public SplitGraphDialog( Shell parent, Object inputMetadata, TransMeta transMeta, String stepname ) {
    super( parent, (BaseStepMeta) inputMetadata, transMeta, stepname );
    input = (SplitGraphMeta) inputMetadata;

    // Hack the metastore...
    //
    metaStore = Spoon.getInstance().getMetaStore();
  }

  @Override public String open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN );
    props.setLook( shell );
    setShellImage( shell, input );

    FormLayout shellLayout = new FormLayout();
    shell.setLayout( shellLayout );
    shell.setText( "Neo4j SplitGraph" );

    ModifyListener lsMod = e -> input.setChanged();
    changed = input.hasChanged();

    ScrolledComposite wScrolledComposite = new ScrolledComposite( shell, SWT.V_SCROLL| SWT.H_SCROLL );
    FormLayout scFormLayout = new FormLayout();
    wScrolledComposite.setLayout( scFormLayout );
    FormData fdSComposite = new FormData();
    fdSComposite.left = new FormAttachment( 0, 0 );
    fdSComposite.right = new FormAttachment( 100, 0 );
    fdSComposite.top = new FormAttachment( 0, 0 );
    fdSComposite.bottom = new FormAttachment( 100, 0 );
    wScrolledComposite.setLayoutData( fdSComposite );

    Composite wComposite = new Composite( wScrolledComposite, SWT.NONE );
    props.setLook( wComposite );
    FormData fdComposite = new FormData();
    fdComposite.left = new FormAttachment( 0, 0 );
    fdComposite.right =  new FormAttachment( 100, 0 );
    fdComposite.top =  new FormAttachment( 0, 0 );
    fdComposite.bottom =  new FormAttachment( 100, 0 );
    wComposite.setLayoutData( fdComposite );

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;
    wComposite.setLayout( formLayout );

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Step name line
    //
    Label wlStepname = new Label( wComposite, SWT.RIGHT );
    wlStepname.setText( "Step name" );
    props.setLook( wlStepname );
    fdlStepname = new FormData();
    fdlStepname.left = new FormAttachment( 0, 0 );
    fdlStepname.right = new FormAttachment( middle, -margin );
    fdlStepname.top = new FormAttachment( 0, margin );
    wlStepname.setLayoutData( fdlStepname );
    wStepname = new Text( wComposite, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wStepname );
    wStepname.addModifyListener( lsMod );
    fdStepname = new FormData();
    fdStepname.left = new FormAttachment( middle, 0 );
    fdStepname.top = new FormAttachment( wlStepname, 0, SWT.CENTER );
    fdStepname.right = new FormAttachment( 100, 0 );
    wStepname.setLayoutData( fdStepname );
    Control lastControl = wStepname;
    
    String[] fieldnames = new String[] {};
    try {
      fieldnames = transMeta.getPrevStepFields(stepMeta).getFieldNames();
    } catch ( KettleStepException e ) {
      log.logError("error getting input field names: ", e);
    }
    
    // Graph input field
    //
    Label wlGraphField = new Label( wComposite, SWT.RIGHT );
    wlGraphField.setText( "Graph field " );
    props.setLook( wlGraphField );
    FormData fdlGraphField = new FormData();
    fdlGraphField.left = new FormAttachment( 0, 0 );
    fdlGraphField.right = new FormAttachment( middle, -margin );
    fdlGraphField.top = new FormAttachment( lastControl, 2 * margin );
    wlGraphField.setLayoutData( fdlGraphField );
    wGraphField = new CCombo( wComposite, SWT.CHECK | SWT.BORDER );
    wGraphField.setItems( fieldnames );
    props.setLook( wGraphField );
    FormData fdGraphField = new FormData();
    fdGraphField.left = new FormAttachment( middle, 0 );
    fdGraphField.right = new FormAttachment( 100, 0 );
    fdGraphField.top = new FormAttachment( wlGraphField, 0, SWT.CENTER );
    wGraphField.setLayoutData( fdGraphField );
    lastControl = wGraphField;
    
    // Type output field
    //
    Label wlTypeField = new Label( wComposite, SWT.RIGHT );
    wlTypeField.setText( "Type output field (Node/Relationship) " );
    props.setLook( wlTypeField );
    FormData fdlTypeField = new FormData();
    fdlTypeField.left = new FormAttachment( 0, 0 );
    fdlTypeField.right = new FormAttachment( middle, -margin );
    fdlTypeField.top = new FormAttachment( lastControl, 2 * margin );
    wlTypeField.setLayoutData( fdlTypeField );
    wTypeField = new TextVar( transMeta, wComposite, SWT.CHECK | SWT.BORDER );
    props.setLook( wTypeField );
    FormData fdTypeField = new FormData();
    fdTypeField.left = new FormAttachment( middle, 0 );
    fdTypeField.right = new FormAttachment( 100, 0 );
    fdTypeField.top = new FormAttachment( wlTypeField, 0, SWT.CENTER );
    wTypeField.setLayoutData( fdTypeField );
    lastControl = wTypeField;

    // The ID of the node or relationship
    //
    Label wlIdField = new Label( wComposite, SWT.RIGHT );
    wlIdField.setText( "ID output field " );
    props.setLook( wlIdField );
    FormData fdlIdField = new FormData();
    fdlIdField.left = new FormAttachment( 0, 0 );
    fdlIdField.right = new FormAttachment( middle, -margin );
    fdlIdField.top = new FormAttachment( lastControl, 2 * margin );
    wlIdField.setLayoutData( fdlIdField );
    wIdField = new TextVar(transMeta, wComposite, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wIdField );
    wIdField.addModifyListener( lsMod );
    FormData fdIdField = new FormData();
    fdIdField.left = new FormAttachment( middle, 0 );
    fdIdField.right = new FormAttachment( 100, 0 );
    fdIdField.top = new FormAttachment( wlIdField, 0, SWT.CENTER );
    wIdField.setLayoutData( fdIdField );
    lastControl = wIdField;

    // The property set (type/name of node, relationship, ...)
    //
    Label wlPropertySetField = new Label( wComposite, SWT.RIGHT );
    wlPropertySetField.setText( "Property set output field " );
    props.setLook( wlPropertySetField );
    FormData fdlPropertySetField = new FormData();
    fdlPropertySetField.left = new FormAttachment( 0, 0 );
    fdlPropertySetField.right = new FormAttachment( middle, -margin );
    fdlPropertySetField.top = new FormAttachment( lastControl, 2 * margin );
    wlPropertySetField.setLayoutData( fdlPropertySetField );
    wPropertySetField = new TextVar(transMeta, wComposite, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wPropertySetField );
    wPropertySetField.addModifyListener( lsMod );
    FormData fdPropertySetField = new FormData();
    fdPropertySetField.left = new FormAttachment( middle, 0 );
    fdPropertySetField.right = new FormAttachment( 100, 0 );
    fdPropertySetField.top = new FormAttachment( wlPropertySetField, 0, SWT.CENTER );
    wPropertySetField.setLayoutData( fdPropertySetField );
    lastControl = wPropertySetField;

    // Some buttons
    wOK = new Button( wComposite, SWT.PUSH );
    wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
    wCancel = new Button( wComposite, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

    // Position the buttons at the bottom of the dialog.
    //
    setButtonPositions( new Button[] { wOK, wCancel }, margin, lastControl );

    wComposite.pack();
    Rectangle bounds = wComposite.getBounds();

    wScrolledComposite.setContent( wComposite );

    wScrolledComposite.setExpandHorizontal( true );
    wScrolledComposite.setExpandVertical( true );
    wScrolledComposite.setMinWidth( bounds.width );
    wScrolledComposite.setMinHeight( bounds.height );

    // Add listeners
    //
    wCancel.addListener( SWT.Selection, e->cancel() );
    wOK.addListener( SWT.Selection, e->ok() );

    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected( SelectionEvent e ) {
        ok();
      }
    };

    wStepname.addSelectionListener( lsDef );
    wGraphField.addSelectionListener( lsDef );
    wTypeField.addSelectionListener( lsDef );
    wIdField.addSelectionListener( lsDef );
    wPropertySetField.addSelectionListener( lsDef );

    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );

    getData();
    input.setChanged( changed );

    shell.open();

    // Set the shell size, based upon previous time...
    setSize();

    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return stepname;

  }

  private void cancel() {
    stepname = null;
    input.setChanged( changed );
    dispose();
  }

  public void getData() {

    wStepname.setText( Const.NVL( stepname, "" ) );
    wGraphField.setText( Const.NVL( input.getGraphField(), "" ) );
    wTypeField.setText( Const.NVL( input.getTypeField(), "" ) );
    wIdField.setText( Const.NVL( input.getIdField(), "" ) );
    wPropertySetField.setText( Const.NVL( input.getPropertySetField(), "" ) );
  }

  private void ok() {
    if ( StringUtils.isEmpty( wStepname.getText() ) ) {
      return;
    }
    stepname = wStepname.getText(); // return value
    getInfo(input);
    dispose();
  }

  private void getInfo( SplitGraphMeta meta) {
    meta.setGraphField( wGraphField.getText() );
    meta.setTypeField( wTypeField.getText() );
    meta.setIdField( wIdField.getText() );
    meta.setPropertySetField( wPropertySetField.getText() );
  }

}
