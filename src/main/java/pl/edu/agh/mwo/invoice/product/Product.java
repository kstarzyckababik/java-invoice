package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product  {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        if (name ==null || name.isBlank()){
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        this.name = name;
        if (price == null || price.compareTo(BigDecimal.ZERO) <0 ){
            throw new IllegalArgumentException("Product price cannot be null or less than zero");
        }
        this.price = price;
        this.taxPercent = tax;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getTaxPercent() {
        return this.taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return this.price.add(price.multiply(this.taxPercent));
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product other = (Product) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
