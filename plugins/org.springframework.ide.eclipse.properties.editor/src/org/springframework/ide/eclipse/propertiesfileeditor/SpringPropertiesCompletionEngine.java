/*******************************************************************************
 * Copyright (c) 2014 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.propertiesfileeditor;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.IDocument;
import org.springframework.configurationmetadata.ConfigurationMetadataProperty;
import org.springframework.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.ide.eclipse.propertiesfileeditor.FuzzyMap.Match;

/**
 * @author Kris De Volder
 */
public class SpringPropertiesCompletionEngine {
		
	private FuzzyMap<ConfigurationMetadataProperty> properties = new FuzzyMap<ConfigurationMetadataProperty>() {
		protected String getKey(ConfigurationMetadataProperty entry) {
			return entry.getId();
		}
	};
	public SpringPropertiesCompletionEngine(IJavaProject jp) throws Exception {
		StsConfigMetadataRepositoryJsonLoader loader = new StsConfigMetadataRepositoryJsonLoader();
		ConfigurationMetadataRepository metadata = loader.load(jp); //TODO: is this fast enough? Or should it be done in background?
		
		Collection<ConfigurationMetadataProperty> allEntries = metadata.getAllProperties().values();
		for (ConfigurationMetadataProperty item : allEntries) {
			properties.add(item);
		}
		
//		properties.dump();
		
		System.out.println(">>> spring properties metadata loaded ===");
		for (Match<ConfigurationMetadataProperty> entry : properties.find("")) {
			System.out.println(entry.data.getId());
		}
//		dumpPropTree(0, metadata);
//		System.out.println("<<< spring properties metadata loaded ===");
	}

	public Collection<Match<ConfigurationMetadataProperty>> getCompletions(IDocument doc, final String prefix, int offset) {
		return properties.find(prefix);
	}

//	/**
//	 * For debuggin. Dump properties in repo in textually formatted 'tree'
//	 */
//	private void dumpPropTree(int i, ConfigMetadataRepository metadata) {
//		Map<String, ConfigMetadataItem> items = metadata.getItems();
//		for (Entry<String, ConfigMetadataItem> entry : items.entrySet()) {
//			println(i, entry.getKey()+ " ITEM");
//			ConfigMetadataItem item = entry.getValue();
//			println(i+2, "id   = "+item.getId());
//			println(i+2, "name = "+item.getName());
//		}
//		
//		for (Entry<String, ConfigMetadataGroup> entry : metadata.getGroups().entrySet()) {
//			println(i, entry.getKey()+ " GROUP");
//			ConfigMetadataGroup item = entry.getValue();
//			println(i+2, "id   = "+item.getId());
//			println(i+2, "name = "+item.getName());
//			dumpPropTree(i+1, item);
//		}
//	}
//
//	private void println(int i, String string) {
//		for (int j = 0; j < i; j++) {
//			System.out.print("  ");
//		}
//		System.out.println(string);
//	}
}

//Mock implementation (makes suggestions from a hard-coded list of words.
//
//public class SpringPropertiesCompletionEngine {
//	
//	private String[] WORDS = {
//		"bar",
//		"bartentender",
//		"bartering",
//		"barren",
//		"banana",
//		"bar.mitswa"
//	};
//	
//	private IJavaProject javaProject;
//
//	public SpringPropertiesCompletionEngine(IJavaProject jp) {
//		this.javaProject = jp;
//	}
//
//	public Collection<String> getCompletions(IJavaProject javaProject, IDocument doc, String prefix, int offset) {
//		ArrayList<String> completions = new ArrayList<>();
//		for (String word : WORDS) {
//			if (word.startsWith(prefix)) {
//				completions.add(word.substring(prefix.length()));
//			}
//		}
//		return completions;
//	}
//
//}