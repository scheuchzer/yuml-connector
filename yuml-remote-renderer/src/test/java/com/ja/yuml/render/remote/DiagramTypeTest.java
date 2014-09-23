package com.ja.yuml.render.remote;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DiagramTypeTest {

	@Test
	public void testClassDiagram() {
		List<String> dsls = new ArrayList<>();
		dsls.add("[Customer]->[Billing Address]");
		dsls.add("[Customer]1-0..*[Address]");
		dsls.add("[Order]-billing >[Address], [Order]-shipping >[Address]");
		dsls.add("[\"Customer\"{bg:orange}]a- b>[Order{bg:green}]");
		dsls.add("[Company]<>-1>[Location], [Location]+->[Point]");
		dsls.add("[Company]++-1>[Location]");
		dsls.add("[Customer]<>1->*[Order], [Customer]-[note: Aggregate Root{bg:cornsilk}]");
		dsls.add("[note: You can stick notes on diagrams too!{bg:cornsilk}],[Customer]<>1-orders 0..*>[Order], [Order]++*-*>[LineItem], [Order]-1>[DeliveryMethod], [Order]*-*>[Product], [Category]<->[Product], [DeliveryMethod]^[National], [DeliveryMethod]^[International]");

		for (String dsl : dsls) {
			assertThat(DiagramType.detect(dsl), is(DiagramType.classDiagram));
		}
	}

	@Test
	public void testActivityDiagram() {
		List<String> dsls = new ArrayList<>();
		dsls.add("(start)->(Boil Kettle)->(end)	");
		dsls.add("(start)-><a>[kettle empty]->(Fill Kettle)->(Boil Kettle),<a>[kettle full]->(Boil Kettle)->(end)");
		dsls.add("(start)-><a>[kettle empty]->(Fill Kettle)->|b|,<a>[kettle full]->|b|->(Boil Kettle)->|c|,|b|->(Add Tea Bag)->(Add Milk)->|c|->(Pour Water)->(end),(Pour Water)->(end)");
		dsls.add("(start)->[Water]->(Fill Kettle)->(end)");
		dsls.add("(start)-fill>(Fill Kettle)->(end)");

		for (String dsl : dsls) {
			assertThat(DiagramType.detect(dsl), is(DiagramType.activityDiagram));
		}
	}

	@Test
	public void testUseCaseDiagram() {
		List<String> dsls = new ArrayList<>();
		dsls.add("[Customer]-(Login)");
		dsls.add("[Customer]-(Login), [Customer]-(note: Cust can be registered or not{bg:beige})");
		dsls.add("[Customer]-(Login), [Customer]-(Logout)");
		dsls.add("[Cms Admin]^[User]");
		dsls.add("[Cms Admin]^[User], [Customer]^[User], [Agent]^[User]");
		dsls.add("(Login)<(Register), (Login)<(Request Password Reminder)");
		dsls.add("(Register)>(Confirm Registration)");
		dsls.add("(note: figure 1.2{bg:beige}), [User]-(Login),[Site Maintainer]-(Add User),(Add User)<(Add Company),[Site Maintainer]-(Upload Docs),(Upload Docs)<(Manage Folders),[User]-(Upload Docs), [User]-(Full Text Search Docs), (Full Text Search Docs)>(Preview Doc),(Full Text Search Docs)>(Download Docs), [User]-(Browse Docs), (Browse Docs)>(Preview Doc), (Download Docs), [Site Maintainer]-(Post New Event To The Web Site), [User]-(View Events)");

		for (String dsl : dsls) {
			System.out.println(dsl);
			assertThat(DiagramType.detect(dsl), is(DiagramType.useCaseDiagram));
		}
	}
}
