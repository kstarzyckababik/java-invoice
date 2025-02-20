package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    //private Collection<Product> products;

    private Map<Product, Integer> products = new HashMap<>();

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
        products.put(product, quantity);
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

}


