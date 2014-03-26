/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package chip;

import generic.EInferer;
import generic.GenericView;
import generic.ImportSpec;
import generic.ImportSpec.CategoricalColumnSpec;
import generic.ImportSpec.ColumnSpec;
import generic.ImportSpec.DoubleColumnSpec;
import generic.ImportSpec.StringColumnSpec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.caleydo.core.util.color.Color;
import org.caleydo.core.util.color.ColorBrewer;

import com.google.common.collect.ImmutableSet;

import demo.RankTableDemo.IModelBuilder;
import demo.project.model.RankTableSpec;

/*
 * @author Samuel Gratzl
 *
 */
public class ChipView extends GenericView {
	private static final String ID = "lineup.demo.chip";
	/**
	 *
	 */
	public ChipView() {
		super(createSpec());
	}

	@Override
	public IModelBuilder createModel(RankTableSpec tableSpec) {
		return super.createModel(tableSpec);
	}
	/**
	 * @return
	 */
	private static ImportSpec createSpec() {
		ImportSpec spec = new ImportSpec();
		Path temp;
		try {
			temp = Files.createTempFile("lineup", ".csv");
			Files.copy(ChipView.class.getResourceAsStream("chip.csv"), temp, StandardCopyOption.REPLACE_EXISTING);
			spec.setDataSourcePath(temp.toString());
			spec.setDelimiter(";");
			spec.setLabel("Chip Handy Bestenliste");
			List<ColumnSpec> columns = new ArrayList<>();
			createColumns(columns);
			spec.setColumns(columns);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return spec;
	}

	/**
	 * @param columns
	 */
	private static void createColumns(List<ColumnSpec> columns) {
		int i = 0;
		Deque<Color> f = new ArrayDeque<>(ColorBrewer.Set3.get(12));
		f.addAll(ColorBrewer.Set2.get(8));
		// Produkt
		columns.add(cstring(i++));
		// Produkt;Preis
		columns.add(cdouble(i++, 0, Double.NaN).setColor(f.pollFirst(), null));
		// Produkt;Preis;Gesamtwertung
		columns.add(cdouble(i++, 0, 100).setColor(f.pollFirst(), null));
		// Produkt;Preis;Gesamtwertung;Preis-Leistung
		columns.add(cdouble(i++, 0, 100).setColor(f.pollFirst(), null));
		// Produkt;Preis;Gesamtwertung;Preis-Leistung;Telefon und Akku
		columns.add(cdouble(i++, 0, 100).setColor(f.pollFirst(), null));
		// Produkt;Preis;Gesamtwertung;Preis-Leistung;Telefon und Akku;Internet
		columns.add(cdouble(i++, 0, 100).setColor(f.pollFirst(), null));
		// ;Multimedia
		columns.add(cdouble(i++, 0, 100).setColor(f.pollFirst(), null));
		// ;Handling;App-Store;Getestet mit;App-Store-Anbindung;Prozessor;SAR-Wert ( W/kg);Akku: Online-Laufzeit
		// (Stunden);Akku: Online-Zeit;Gewicht (Gramm);WLAN;UMTS: Daten empfangen;Display: Typ;Display: Diagonale
		// (Zoll);Display: Auflösung;Display: Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(cdouble(i++, 0, 100).setColor(f.pollFirst(), null));
		// ;App-Store;Getestet mit;App-Store-Anbindung;Prozessor;SAR-Wert ( W/kg);Akku: Online-Laufzeit (Stunden);Akku:
		// Online-Zeit;Gewicht (Gramm);WLAN;UMTS: Daten empfangen;Display: Typ;Display: Diagonale (Zoll);Display:
		// Auflösung;Display: Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(cdouble(i++, 0, 100).setColor(f.pollFirst(), null));
		// ;Getestet mit;App-Store-Anbindung;Prozessor;SAR-Wert ( W/kg);Akku: Online-Laufzeit (Stunden);Akku:
		// Online-Zeit;Gewicht (Gramm);WLAN;UMTS: Daten empfangen;Display: Typ;Display: Diagonale (Zoll);Display:
		// Auflösung;Display: Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		i++;
		// ;App-Store-Anbindung;Prozessor;SAR-Wert ( W/kg);Akku: Online-Laufzeit (Stunden);Akku: Online-Zeit;Gewicht
		// (Gramm);WLAN;UMTS: Daten empfangen;Display: Typ;Display: Diagonale (Zoll);Display: Auflösung;Display:
		// Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(category(i++, "Apple App Store", "BlackBerry App World", "Google Play", "Ovi Store",
				"Samsung Apps", "Windows Phone Store").setColor(f.pollFirst(), null));
		// ;Prozessor;SAR-Wert ( W/kg);Akku: Online-Laufzeit (Stunden);Akku: Online-Zeit;Gewicht (Gramm);WLAN;UMTS:
		// Daten empfangen;Display: Typ;Display: Diagonale (Zoll);Display: Auflösung;Display: Pixeldichte (ppi);Speicher
		// (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		i++;
		// ;SAR-Wert ( W/kg);Akku: Online-Laufzeit (Stunden);Akku: Online-Zeit;Gewicht (Gramm);WLAN;UMTS: Daten
		// empfangen;Display: Typ;Display: Diagonale (Zoll);Display: Auflösung;Display: Pixeldichte (ppi);Speicher
		// (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(cdouble(i++, Double.NaN, Double.NaN).setColor(f.pollFirst(), null));
		// ;Akku: Online-Laufzeit (Stunden);Akku: Online-Zeit;Gewicht (Gramm);WLAN;UMTS: Daten empfangen;Display:
		// Typ;Display: Diagonale (Zoll);Display: Auflösung;Display: Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung
		// (Mpixel);UKW-Radio
		i++;
		// ;Akku: Online-Zeit;Gewicht (Gramm);WLAN;UMTS: Daten empfangen;Display: Typ;Display: Diagonale (Zoll);Display:
		// Auflösung;Display: Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(cdouble(i++, Double.NaN, Double.NaN).setColor(f.pollFirst(), null));
		// ;Gewicht (Gramm);WLAN;UMTS: Daten empfangen;Display: Typ;Display: Diagonale (Zoll);Display:
		// Auflösung;Display: Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(cdouble(i++, Double.NaN, Double.NaN).setColor(f.pollFirst(), null));
		// ;WLAN;UMTS: Daten empfangen;Display: Typ;Display: Diagonale (Zoll);Display: Auflösung;Display: Pixeldichte
		// (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(category(i++, "802.11ac/n/g/b/a", "802.11g/b", "802.11n/g/b", "802.11n/g/b/a"));
		// ;UMTS: Daten empfangen;Display: Typ;Display: Diagonale (Zoll);Display: Auflösung;Display: Pixeldichte
		// (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		i++;
		// ;Display: Typ;Display: Diagonale (Zoll);Display: Auflösung;Display: Pixeldichte (ppi);Speicher (GB);Kamera:
		// Auflösung (Mpixel);UKW-Radio
		columns.add(category(i++, "LCD", "OLED"));
		// ;Display: Diagonale (Zoll);Display: Auflösung;Display: Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung
		// (Mpixel);UKW-Radio
		i++; // columns.add(cdouble(i++, Double.NaN, Double.NaN).setColor(f.pollFirst(), null));
		// ;Display: Auflösung;Display: Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(category(i++, "1080 x 1920 Pixel", "240 x 320 Pixel", "240 x 400 Pixel", "320 x 480 Pixel",
				"360 x 640 Pixel", "450 x 854 Pixel", "480 x 800 Pixel", "480 x 854 Pixel", "540 x 960 Pixel",
				"640 x 1136 Pixel", "640 x 960 Pixel", "720 x 1280 Pixel", "720 x 720 Pixel", "768 x 1024 Pixel",
				"768 x 1280 Pixel", "800 x 1280 Pixel").setColor(f.pollFirst(), null));
		// ;Display: Pixeldichte (ppi);Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(cdouble(i++, Double.NaN, Double.NaN).setColor(f.pollFirst(), null));
		// ;Speicher (GB);Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(cdouble(i++, Double.NaN, Double.NaN).setColor(f.pollFirst(), null));
		// ;Kamera: Auflösung (Mpixel);UKW-Radio
		columns.add(cdouble(i++, Double.NaN, Double.NaN).setColor(f.pollFirst(), null));
		// ;UKW-Radio
		columns.add(category(i++, "-", "ja", "Mit RDS"));

	}

	private static ColumnSpec cdouble(int col, double min, double max) {
		DoubleColumnSpec s = new DoubleColumnSpec();
		s.setCol(col);
		s.setMapping(min, max, EInferer.NaN);
		return s;
	}

	private static ColumnSpec category(int col, String... categories) {
		CategoricalColumnSpec s = new CategoricalColumnSpec(ImmutableSet.copyOf(categories));
		s.setCol(col);
		return s;
	}

	private static ColumnSpec cstring(int col) {
		return new StringColumnSpec(col);
	}
}

