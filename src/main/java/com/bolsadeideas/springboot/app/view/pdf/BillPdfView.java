package com.bolsadeideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.springboot.app.models.entity.Bill;
import com.bolsadeideas.springboot.app.models.entity.ItemBill;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("bill/see")
public class BillPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Bill bill = (Bill) model.get("bill");
		
		PdfPCell cell = new PdfPCell(new Phrase("Client Data"));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(5f);
		
		PdfPTable tableDataClient = new PdfPTable(1);
		tableDataClient.addCell(cell);
		tableDataClient.setSpacingAfter(15);
		tableDataClient.addCell(bill.getClient().getName() + " " + bill.getClient().getLastName());
		tableDataClient.addCell(bill.getClient().getEmail());
		
		cell.setPhrase(new Phrase("Bill Data"));
		
		PdfPTable tableDataBill = new PdfPTable(1);
		tableDataBill.addCell(cell);
		tableDataBill.setSpacingAfter(25);
		tableDataBill.addCell("Folio : " + bill.getId());
		tableDataBill.addCell("Description: " + bill.getDescription());
		tableDataBill.addCell("Date: " + bill.getCreateAt());
		
		document.add(tableDataClient);
		document.add(tableDataBill);
		
		
		PdfPTable tableProducts = new PdfPTable(4);
		tableProducts.setSpacingAfter(25);
		tableProducts.addCell("Product: ");
		tableProducts.setWidths(new float[] {2.5f, 1, 1, 1});
		tableProducts.addCell("Price: ");
		tableProducts.addCell("Quantity: ");
		tableProducts.addCell("Total: ");
		
		for(ItemBill item: bill.getItems()) {
			tableProducts.addCell(item.getProduct().getName());
			tableProducts.addCell(item.getProduct().getPrice().toString());
			tableProducts.addCell(item.getQuantity().toString());
			tableProducts.addCell(item.calculateAmount().toString());
		}
		
		cell = new PdfPCell(new Phrase("Total: "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableProducts.addCell(cell);
		tableProducts.addCell(bill.getTotal().toString());
		
		document.add(tableProducts);
	}
}