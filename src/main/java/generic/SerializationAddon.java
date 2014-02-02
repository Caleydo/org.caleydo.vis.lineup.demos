/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package generic;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.caleydo.core.data.datadomain.IDataDomain;
import org.caleydo.core.serialize.ISerializationAddon;
import org.caleydo.core.serialize.SerializationData;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.google.common.io.Files;

import demo.handler.ShowWizardHandler;

/**
 * @author Samuel Gratzl
 *
 */
public class SerializationAddon implements ISerializationAddon {

	@Override
	public Collection<? extends Class<?>> getJAXBContextClasses() {
		return Collections.singleton(ImportSpec.class);
	}

	@Override
	public void deserialize(String dirName, Unmarshaller unmarshaller) {
	}

	@Override
	public void deserialize(String dirName, Unmarshaller unmarshaller, SerializationData data) {
		ImportSpecs spec;
		try {
			spec = (ImportSpecs) unmarshaller.unmarshal(new File(dirName, "importSpecs.xml"));
			data.setAddonData("lineup", spec);
			data.setAddonData("lineupDir", dirName);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void serialize(Collection<? extends IDataDomain> toSave, Marshaller marshaller, String dirName)
			throws IOException {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		ImportSpecs specs = new ImportSpecs();
		for(IWorkbenchWindow w : workbench.getWorkbenchWindows()) {
			for(IWorkbenchPage page : w.getPages()) {
				for (IViewReference r : page.getViewReferences()) {
					IViewPart view = r.getView(false);
					if (!(view instanceof GenericView))
						continue;
					specs.add(((GenericView) view).getSpec());
				}
			}
		}

		int i = 0;
		for (ImportSpec spec : specs) {
			File f = new File(spec.getDataSourcePath());
			File out = new File(dirName, String.format("importSpecFile%d.csv", i++));
			Files.copy(f, out);
		}
		try {
			marshaller.marshal(specs, new File(dirName, "importSpecs.xml"));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void load(SerializationData data) {
		ImportSpecs specs = (ImportSpecs) data.getAddonData("lineup");
		if (specs == null)
			return;
		int i = 0;
		String dirName = data.getAddonData("lineupDir").toString();
		for (ImportSpec spec : specs) {
			File f = new File(dirName, String.format("importSpecFile%d.csv", i++));
			spec.setDataSourcePath(f.getAbsolutePath());

			ShowWizardHandler.showView(spec);
		}
	}

}
