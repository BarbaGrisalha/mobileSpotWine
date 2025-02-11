package pt.ipleiria.estg.dei.amsi.mobilesportwine.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Invoice;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class InvoiceJsonParser {
    public static ArrayList<Invoice> parserJsonInvoices(JSONArray response) {
        ArrayList<Invoice> invoices = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);

                int invoiceId = obj.getInt("invoice_id");
                String invoiceNumber = obj.getString("invoice_number");
                String invoiceDate = obj.getString("invoice_date");
                String totalAmount = obj.getString("total_amount");
                String status = obj.getString("status");

                // Parse dos vinhos
                List<Vinho> vinhos = new ArrayList<>();
                JSONArray vinhosArray = obj.getJSONArray("products");
                for (int j = 0; j < vinhosArray.length(); j++) {
                    JSONObject vinhoObj = vinhosArray.getJSONObject(j);
                    int id = vinhoObj.getInt("product_id");
                    String name = vinhoObj.getString("product_name");
                    int quantity = vinhoObj.getInt("quantity");
                    double price = Double.parseDouble(vinhoObj.getString("unit_price").replace(",", "."));
                    String image = ""; // Adicione o campo de imagem se necessário

                    vinhos.add(new Vinho(id, name, "", "", price, quantity, image));
                }

                invoices.add(new Invoice(invoiceId, invoiceNumber, invoiceDate, totalAmount, status, vinhos));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return invoices;
    }
}