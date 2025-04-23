package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.*;

import static org.junit.Assert.*;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetPrice()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTax()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossPrice()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetPrice()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetPrice()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getGrossPrice(), Matchers.comparesEqualTo(invoice.getNetPrice()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetPrice()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTax()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossPrice()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetPrice()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossPrice()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void invoiceNumber() {
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();

        assertNotNull(invoice1.getNumber());
        assertNotNull(invoice2.getNumber());
        assertNotEquals(invoice1.getNumber(), invoice2.getNumber());
    }


    @Test
    public void invoiceShouldPrintProductsListCorrectly() {
        Invoice invoice = new Invoice();
        Product product1 = new DairyProduct("mleko", new BigDecimal("10.00"));
        Product product2 = new DairyProduct("jogurcik", new BigDecimal("20.00"));

        invoice.addProduct(product1, 2);
        invoice.addProduct(product2, 1);

        int invoiceNumber = invoice.getNumber();


        String expectedPrintout = "Invoice number: " + invoiceNumber + "\n" +
                "mleko, 2, 10.00\n" +
                "jogurcik, 1, 20.00\n" +
                "Number of items: 2";

        assertEquals(expectedPrintout, invoice.printInvoice());
    }


    @Test
    public void invoiceDuplicateProduct() {
        Invoice invoice = new Invoice();
        Product product1 = new DairyProduct("mleko", new BigDecimal("10.00"));
        Product product2 = new DairyProduct("jogurcik", new BigDecimal("20.00"));


        invoice.addProduct(product1, 2);
        invoice.addProduct(product2, 1);
        invoice.addProduct(product1,2);

        int invoiceNumber = invoice.getNumber();


        String expectedPrintout = "Invoice number: " + invoiceNumber + "\n" +
                "mleko, 4, 10.00\n" +
                "jogurcik, 1, 20.00\n" +
                "Number of items: 2";

        assertEquals(expectedPrintout, invoice.printInvoice());
    }


    @Test
    public void taxShouldIncludeExciseDutyForWineAndFuel() {
        Product wine = new BottleOfWine("Red Wine", new BigDecimal("20.00"), new BigDecimal("0.23"));
        Product fuel = new FuelCanister("Diesel", new BigDecimal("100.00"));


        Invoice invoice = new Invoice();
        invoice.addProduct(wine, 1);
        invoice.addProduct(fuel, 1);


        BigDecimal expectedTax =
                new BigDecimal("10.16").add(new BigDecimal("5.56")); // Red Wine: 4.6 + 5.56

        assertEquals(0, expectedTax.compareTo(invoice.getTax()));

    }

    @Test
    public void shouldCalculateCorrectGrossPriceForWineAndFuel() {
        Product wine = new BottleOfWine("Merlot", new BigDecimal("20.00"), new BigDecimal("0.23")); // 20 + 4.6 + 5.56
        Product fuel = new FuelCanister("Diesel", new BigDecimal("100.00")); // 100 + 5.56

        assertEquals(0, new BigDecimal("30.16").compareTo(wine.getPriceWithTax()));
        assertEquals(0, new BigDecimal("105.56").compareTo(fuel.getPriceWithTax()));

    }







}



