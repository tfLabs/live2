package org.xtext.example.mydsl.generator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.xtext.example.mydsl.md2.Greeting;
import org.xtext.example.mydsl.md2.Model;

public class Md2Converter {

	public String createContents(Resource resource) {
		StringBuilder sb = new StringBuilder(10);
		EObject obj = resource.getContents().get(0);
		if (obj instanceof Model) {
			Model model = (Model)obj;
			for(Greeting g :model.getGreetings()) {
				String hello = "Hello " + g.getName() + "!!";
				sb.append(hello + "\n");
			}
		}
		return sb.toString();
	}

}
