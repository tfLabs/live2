package test.rcp.part;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.IXtextModelListener;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.xtext.example.mydsl.generator.Md2Converter;

import com.google.inject.Inject;

public class Preview {
	@Inject
	private EObjectAtOffsetHelper helper = new EObjectAtOffsetHelper();
	
	private Text text = null;
	private ArrayList<IEditorPart> editors = new ArrayList<IEditorPart>();
	
	public Preview() {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        workbenchWindow.getPartService().addPartListener(new IPartListener() {
			
			@Override
			public void partOpened(IWorkbenchPart arg0) {
				if (arg0 instanceof XtextEditor) {
					if (!editors.contains(arg0)) {
						XtextEditor x = (XtextEditor) arg0;
						editors.add(x);
						IXtextDocument d = x.getDocument();
						d.addModelListener(new IXtextModelListener() {
							@Override
							public void modelChanged(XtextResource resource) {
								setText(resource);
							}
						});
						int offset = 0;
						EObject object = d.readOnly(new IUnitOfWork<EObject, XtextResource>() {
							@Override
							public EObject exec(XtextResource state) throws Exception {
								return helper.resolveContainedElementAt(state, offset);
							}
						});
						if (object != null) {
							Resource r = object.eResource();
							if (r instanceof XtextResource) {
								setText((XtextResource) r);
							}
						}
					}
				}
			}
			
			@Override
			public void partDeactivated(IWorkbenchPart arg0) {
			}
			
			@Override
			public void partClosed(IWorkbenchPart arg0) {
		    	if (arg0 instanceof XtextEditor) {
		    		if (editors.contains(arg0)) {
		    			XtextEditor x = (XtextEditor)arg0;
		    			editors.remove(x);
		    			if (editors.isEmpty()) {
		    				setText("");
		    			}
		    		}
		    	}
			}
			
			@Override
			public void partBroughtToTop(IWorkbenchPart arg0) {
			}
			
			@Override
			public void partActivated(IWorkbenchPart arg0) {
				if (arg0 instanceof XtextEditor) {
					XtextEditor x = (XtextEditor) arg0;
					int offset = 0;
					IXtextDocument d = x.getDocument();
					EObject object = d.readOnly(new IUnitOfWork<EObject, XtextResource>() {
						@Override
						public EObject exec(XtextResource state) throws Exception {
							return helper.resolveContainedElementAt(state, offset);
						}
					});
					if (object != null) {
						Resource r = object.eResource();
						if (r instanceof XtextResource) {
							setText((XtextResource) r);
						}
					}
				}
				
			}
		});
		
	}
	
	@PostConstruct
    public void createControls(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
	}
	
    private void setText(XtextResource resource) {
    	if (text != null && !text.isDisposed()) {
        	if (!resource.getParseResult().hasSyntaxErrors()) {
        		StringBuffer sb = new StringBuffer(10);
        		Md2Converter c = new Md2Converter();
        		sb.append(c.createContents(resource));
            	text.setText(sb.toString());
        	}
    	}
    }
    
    private void setText(String str) {
    	if (text != null && !text.isDisposed()) {
    		text.setText(str);
    	}
    }
}
