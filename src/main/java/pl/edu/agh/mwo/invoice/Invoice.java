package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {

    private Map<Product, Integer> products = new LinkedHashMap<>();
    private static int nextNumber = 1;
    private final int number;


    public Invoice() {
        this.number = nextNumber++;
    }


    public void addProduct(Product product) {
        if(product==null){
            throw new IllegalArgumentException("Product cannot be null");
        }
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if(quantity<=0 ){
            throw new IllegalArgumentException("Quantity cannot be 0 and less than 0 ");
        }

        int currentQuantity = products.getOrDefault(product, 0);
        products.put(product, currentQuantity + quantity);
    }


    public BigDecimal getNetPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            Integer quantity = products.get(product);
            sum = sum.add(product.getPrice().multiply(new BigDecimal(quantity)));
        }
        return sum;
    }



    public BigDecimal getTax() {
        return getGrossPrice().subtract(getNetPrice());
    }




    public BigDecimal getGrossPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            Integer quantity = products.get(product);
            sum = sum.add(product.getPriceWithTax().multiply(new BigDecimal(quantity)));
        }
        return sum;
    }



    public int getNumber() {
        return number;
    }

    public String printInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice number: ").append(this.number).append("\n");

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            sb.append(product.getName()).append(", ")
                    .append(quantity).append(", ")
                    .append(product.getPrice()).append("\n");
        }
        int n = products.size();

        sb.append("Number of items: ").append(n);
        return sb.toString();
    }
}


